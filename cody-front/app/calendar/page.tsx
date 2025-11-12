"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { ArrowLeft, CalendarIcon, ChevronLeft, ChevronRight, Loader2 } from "lucide-react"
import { api, type History } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const FORMALITY_COLORS = [
  "bg-[oklch(var(--formality-1))]",
  "bg-[oklch(var(--formality-2))]",
  "bg-[oklch(var(--formality-3))]",
  "bg-[oklch(var(--formality-4))]",
  "bg-[oklch(var(--formality-5))]",
]

interface CalendarData {
  [date: string]: {
    outfit: string
    outfitId: number
    formality: number
  }
}

export default function CalendarPage() {
  const { toast } = useToast()
  const [currentDate, setCurrentDate] = useState(new Date())
  const [histories, setHistories] = useState<History[]>([])
  const [loading, setLoading] = useState(true)
  const [calendarData, setCalendarData] = useState<CalendarData>({})

  // Fetch history data for current month
  useEffect(() => {
    const fetchHistories = async () => {
      try {
        setLoading(true)
        const year = currentDate.getFullYear()
        const month = currentDate.getMonth() + 1 // JavaScript months are 0-indexed

        const data = await api.getHistoriesByMonth(year, month)
        setHistories(data)

        // Convert histories to calendar data
        const calData: CalendarData = {}
        data.forEach((history) => {
          calData[history.wornDate] = {
            outfit: history.outfit.name,
            outfitId: history.outfit.id,
            formality: history.outfit.formalityLevel,
          }
        })
        setCalendarData(calData)
      } catch (err) {
        console.error('Failed to fetch histories:', err)
        toast({
          title: "Error",
          description: "Failed to load calendar data.",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }

    fetchHistories()
  }, [currentDate, toast])

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear()
    const month = date.getMonth()
    const firstDay = new Date(year, month, 1)
    const lastDay = new Date(year, month + 1, 0)
    const daysInMonth = lastDay.getDate()
    const startingDayOfWeek = firstDay.getDay()

    return { daysInMonth, startingDayOfWeek }
  }

  const { daysInMonth, startingDayOfWeek } = getDaysInMonth(currentDate)

  const previousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1))
  }

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1))
  }

  const formatDate = (day: number) => {
    const year = currentDate.getFullYear()
    const month = String(currentDate.getMonth() + 1).padStart(2, "0")
    const dayStr = String(day).padStart(2, "0")
    return `${year}-${month}-${dayStr}`
  }

  const monthName = currentDate.toLocaleDateString("en-US", { month: "long", year: "numeric" })

  // Calculate stats from histories
  const daysTracked = histories.length
  const uniqueOutfits = new Set(histories.map(h => h.outfit.id)).size
  const avgFormality = histories.length > 0
    ? (histories.reduce((sum, h) => sum + h.outfit.formalityLevel, 0) / histories.length).toFixed(1)
    : "0"

  // Find most worn outfit
  const outfitCounts = histories.reduce((acc, h) => {
    const id = h.outfit.id
    acc[id] = (acc[id] || 0) + 1
    return acc
  }, {} as Record<number, number>)
  const mostWornCount = Math.max(...Object.values(outfitCounts), 0)

  // Get today's date string
  const today = new Date()
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-primary animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Loading calendar...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b border-border/50 bg-background/80 backdrop-blur-md sticky top-0 z-10">
        <div className="container mx-auto px-6 py-6">
          <div className="flex items-center gap-4">
            <Button variant="ghost" size="icon" asChild>
              <Link href="/">
                <ArrowLeft className="w-4 h-4" />
              </Link>
            </Button>
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-[oklch(var(--formality-3))]/10 flex items-center justify-center">
                <CalendarIcon className="w-4 h-4 text-[oklch(var(--formality-3))]" />
              </div>
              <h1 className="text-xl font-serif tracking-tight">Outfit Calendar</h1>
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-6xl">
        <div className="flex items-center justify-between mb-8">
          <h2 className="text-3xl font-serif font-light">{monthName}</h2>
          <div className="flex gap-2">
            <Button variant="outline" size="icon" onClick={previousMonth} className="border-border/50 bg-transparent">
              <ChevronLeft className="w-4 h-4" />
            </Button>
            <Button variant="outline" size="icon" onClick={nextMonth} className="border-border/50 bg-transparent">
              <ChevronRight className="w-4 h-4" />
            </Button>
          </div>
        </div>

        <Card className="border border-border/50">
          <CardContent className="p-6">
            {/* Weekday Headers */}
            <div className="grid grid-cols-7 gap-3 mb-3">
              {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
                <div
                  key={day}
                  className="text-center font-light text-sm text-muted-foreground py-3 uppercase tracking-wider"
                >
                  {day}
                </div>
              ))}
            </div>

            {/* Calendar Days */}
            <div className="grid grid-cols-7 gap-3">
              {/* Empty cells for days before month starts */}
              {Array.from({ length: startingDayOfWeek }).map((_, index) => (
                <div key={`empty-${index}`} className="aspect-square" />
              ))}

              {/* Actual days */}
              {Array.from({ length: daysInMonth }).map((_, index) => {
                const day = index + 1
                const dateStr = formatDate(day)
                const outfitData = calendarData[dateStr]
                const isToday = dateStr === todayStr

                return (
                  <Link
                    key={day}
                    href={outfitData ? `/outfits/${outfitData.outfitId}` : "#"}
                    className={`aspect-square border rounded-lg p-3 transition-colors ${
                      outfitData ? "cursor-pointer hover:border-primary/50" : "cursor-default"
                    } ${isToday ? "border-primary" : "border-border/50"}`}
                  >
                    <div className="text-sm font-light mb-2">{day}</div>
                    {outfitData && (
                      <div className="space-y-1.5">
                        <div className={`w-full h-1.5 rounded-full ${FORMALITY_COLORS[outfitData.formality - 1]}`} />
                        <div className="text-xs text-muted-foreground truncate font-light">{outfitData.outfit}</div>
                      </div>
                    )}
                  </Link>
                )
              })}
            </div>
          </CardContent>
        </Card>

        <div className="mt-12">
          <h3 className="text-sm font-light text-muted-foreground mb-6 uppercase tracking-wider">Formality Levels</h3>
          <div className="grid grid-cols-2 md:grid-cols-5 gap-6">
            {[
              { level: 1, label: "Very Casual" },
              { level: 2, label: "Casual" },
              { level: 3, label: "Smart Casual" },
              { level: 4, label: "Business" },
              { level: 5, label: "Formal" },
            ].map(({ level, label }) => (
              <div key={level} className="flex items-center gap-3">
                <div className={`w-3 h-3 rounded-full ${FORMALITY_COLORS[level - 1]}`} />
                <span className="text-sm text-muted-foreground font-light">{label}</span>
              </div>
            ))}
          </div>
        </div>

        <Card className="mt-12 border border-border/50">
          <CardContent className="p-8">
            <h3 className="text-lg font-serif font-light mb-6">This Month's Stats</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
              <div>
                <div className="text-3xl font-serif font-light text-foreground mb-1">{daysTracked}</div>
                <div className="text-sm text-muted-foreground font-light">Days Tracked</div>
              </div>
              <div>
                <div className="text-3xl font-serif font-light text-foreground mb-1">{uniqueOutfits}</div>
                <div className="text-sm text-muted-foreground font-light">Unique Outfits</div>
              </div>
              <div>
                <div className="text-3xl font-serif font-light text-foreground mb-1">{avgFormality}</div>
                <div className="text-sm text-muted-foreground font-light">Avg Formality</div>
              </div>
              <div>
                <div className="text-3xl font-serif font-light text-foreground mb-1">{mostWornCount}</div>
                <div className="text-sm text-muted-foreground font-light">Most Worn</div>
              </div>
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  )
}
