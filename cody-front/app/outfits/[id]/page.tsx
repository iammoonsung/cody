"use client"

import { use, useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Slider } from "@/components/ui/slider"
import { Badge } from "@/components/ui/badge"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { ArrowLeft, Star, Pencil, Trash2, Loader2, Calendar, X } from "lucide-react"
import { api, type Outfit, type History } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const FORMALITY_COLORS = [
  "bg-[oklch(var(--formality-1))]",
  "bg-[oklch(var(--formality-2))]",
  "bg-[oklch(var(--formality-3))]",
  "bg-[oklch(var(--formality-4))]",
  "bg-[oklch(var(--formality-5))]",
]

const FORMALITY_LABELS = ["Home", "Neighborhood", "Outing", "Work", "Formal"]

export default function OutfitDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = use(params)  // Next.js 15+ params unwrapping
  const router = useRouter()
  const { toast } = useToast()
  const [outfit, setOutfit] = useState<Outfit | null>(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [isEditing, setIsEditing] = useState(false)
  const [todayHistory, setTodayHistory] = useState<History | null>(null)
  const [checkingHistory, setCheckingHistory] = useState(false)
  const [formData, setFormData] = useState({
    name: "",
    rating: 3,
    formalityLevel: [3],
  })

  // Fetch outfit data
  useEffect(() => {
    const fetchOutfit = async () => {
      try {
        setLoading(true)
        const data = await api.getOutfit(Number(resolvedParams.id))
        setOutfit(data)
        setFormData({
          name: data.name,
          rating: data.rating,
          formalityLevel: [data.formalityLevel],
        })
      } catch (err) {
        console.error('Failed to fetch outfit:', err)
        toast({
          title: "Error",
          description: "Failed to load outfit details.",
          variant: "destructive",
        })
        router.push("/outfits")
      } finally {
        setLoading(false)
      }
    }

    fetchOutfit()
  }, [resolvedParams.id, router, toast])

  // Check if this outfit is worn today
  useEffect(() => {
    const checkTodayHistory = async () => {
      if (!outfit) return

      try {
        setCheckingHistory(true)
        const histories = await api.getHistoriesByOutfit(outfit.id)
        const today = new Date().toISOString().split('T')[0]
        const found = histories.find(h => h.wornDate === today)
        setTodayHistory(found || null)
      } catch (err) {
        console.error('Failed to check today history:', err)
      } finally {
        setCheckingHistory(false)
      }
    }

    checkTodayHistory()
  }, [outfit])

  const handleSave = async () => {
    setSaving(true)
    try {
      const updatedOutfit = await api.updateOutfit(Number(resolvedParams.id), {
        name: formData.name,
        rating: formData.rating,
        formalityLevel: formData.formalityLevel[0],
        itemIds: outfit!.outfitItems.map(oi => oi.item.id),
      })

      setOutfit(updatedOutfit)
      setIsEditing(false)
      toast({
        title: "Success!",
        description: "Outfit updated successfully",
      })
    } catch (error) {
      console.error('Failed to update outfit:', error)
      toast({
        title: "Error",
        description: "Failed to update outfit. Please try again.",
        variant: "destructive",
      })
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    try {
      await api.deleteOutfit(Number(resolvedParams.id))
      toast({
        title: "Success!",
        description: "Outfit deleted successfully",
      })
      router.push("/outfits")
    } catch (error) {
      console.error('Failed to delete outfit:', error)
      toast({
        title: "Error",
        description: "Failed to delete outfit. Please try again.",
        variant: "destructive",
      })
    }
  }

  const handleWearToday = async () => {
    try {
      const today = new Date().toISOString().split('T')[0]
      await api.recordOutfitWorn(Number(resolvedParams.id), today)

      toast({
        title: "Success!",
        description: "Recorded as worn today. Redirecting to calendar...",
      })

      // Redirect to calendar page after success
      setTimeout(() => {
        router.push("/calendar")
      }, 500)
    } catch (error) {
      console.error('Failed to record outfit worn:', error)
      toast({
        title: "Error",
        description: "Failed to record worn date. Please try again.",
        variant: "destructive",
      })
    }
  }

  const handleChangeToday = async () => {
    try {
      if (!todayHistory) return

      // Delete today's history
      await api.deleteHistory(todayHistory.id)

      toast({
        title: "Changed!",
        description: "Today's record has been removed. You can now select a different outfit.",
      })

      // Update state
      setTodayHistory(null)

      // Refresh outfit data (wornCount changed)
      const updatedOutfit = await api.getOutfit(Number(resolvedParams.id))
      setOutfit(updatedOutfit)
    } catch (error) {
      console.error('Failed to change today outfit:', error)
      toast({
        title: "Error",
        description: "Failed to change today's outfit. Please try again.",
        variant: "destructive",
      })
    }
  }

  const handleCancel = () => {
    if (outfit) {
      setFormData({
        name: outfit.name,
        rating: outfit.rating,
        formalityLevel: [outfit.formalityLevel],
      })
    }
    setIsEditing(false)
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-primary animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Loading outfit...</p>
        </div>
      </div>
    )
  }

  if (!outfit) {
    return null
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b border-border/50 bg-background/80 backdrop-blur-md sticky top-0 z-10">
        <div className="container mx-auto px-6 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Button variant="ghost" size="icon" asChild>
                <Link href="/outfits">
                  <ArrowLeft className="w-4 h-4" />
                </Link>
              </Button>
              <h1 className="text-xl font-serif tracking-tight">Outfit Details</h1>
            </div>
            <div className="flex gap-2">
              {!isEditing ? (
                <>
                  <Button variant="outline" size="sm" onClick={() => setIsEditing(true)}>
                    <Pencil className="w-4 h-4 mr-2" />
                    Edit
                  </Button>
                  <AlertDialog>
                    <AlertDialogTrigger asChild>
                      <Button variant="destructive" size="sm">
                        <Trash2 className="w-4 h-4 mr-2" />
                        Delete
                      </Button>
                    </AlertDialogTrigger>
                    <AlertDialogContent>
                      <AlertDialogHeader>
                        <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                        <AlertDialogDescription>
                          This action cannot be undone. This will permanently delete this outfit from your collection.
                        </AlertDialogDescription>
                      </AlertDialogHeader>
                      <AlertDialogFooter>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                        <AlertDialogAction onClick={handleDelete} className="bg-destructive text-destructive-foreground">
                          Delete
                        </AlertDialogAction>
                      </AlertDialogFooter>
                    </AlertDialogContent>
                  </AlertDialog>
                </>
              ) : (
                <>
                  <Button variant="outline" size="sm" onClick={handleCancel}>
                    Cancel
                  </Button>
                  <Button size="sm" onClick={handleSave} disabled={saving}>
                    {saving && <Loader2 className="w-4 h-4 mr-2 animate-spin" />}
                    {saving ? "Saving..." : "Save Changes"}
                  </Button>
                </>
              )}
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-4xl">
        <div className="grid md:grid-cols-2 gap-8">
          {/* Left: Outfit Items */}
          <Card className="border border-border/50">
            <CardContent className="p-6">
              <h2 className="text-lg font-serif font-light mb-6">Items in this Outfit</h2>
              <div className="grid grid-cols-2 gap-4">
                {outfit.outfitItems.map((outfitItem) => (
                  <div key={outfitItem.id} className="space-y-2">
                    <Badge variant="secondary" className="text-xs font-normal">
                      {outfitItem.item.category}
                    </Badge>
                    <div className="aspect-square overflow-hidden rounded-lg bg-muted/30 border border-border/50">
                      <img
                        src={outfitItem.item.imageUrl || "/placeholder.svg"}
                        alt={outfitItem.item.name}
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <p className="text-xs font-light text-center">{outfitItem.item.name}</p>
                  </div>
                ))}
              </div>

              {/* Stats */}
              <div className="mt-8 pt-6 border-t border-border/50 space-y-4">
                <div className="flex justify-between items-center">
                  <span className="text-sm text-muted-foreground font-light">Worn Count</span>
                  <span className="text-lg font-light">{outfit.wornCount}x</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-sm text-muted-foreground font-light">Last Worn</span>
                  <span className="text-sm font-light">
                    {outfit.lastWornDate ? new Date(outfit.lastWornDate).toLocaleDateString() : 'Never'}
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Right: Outfit Details */}
          <div className="space-y-6">
            <Card className="border border-border/50">
              <CardContent className="p-6 space-y-6">
                {/* Outfit Name */}
                <div className="space-y-3">
                  <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                    Outfit Name
                  </Label>
                  {!isEditing ? (
                    <p className="text-xl font-serif font-light">{formData.name || "Untitled Outfit"}</p>
                  ) : (
                    <Input
                      placeholder="e.g., Office Casual"
                      className="border-border/50"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    />
                  )}
                </div>

                {/* Rating */}
                <div className="space-y-3">
                  <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                    Rating
                  </Label>
                  <div className="flex items-center gap-2">
                    {[1, 2, 3, 4, 5].map((value) => (
                      <button
                        key={value}
                        onClick={() => isEditing && setFormData({ ...formData, rating: value })}
                        className={`transition-colors ${!isEditing ? "cursor-default" : ""}`}
                        type="button"
                        disabled={!isEditing}
                      >
                        <Star
                          className={`w-8 h-8 ${
                            value <= formData.rating ? "fill-primary text-primary" : "text-muted-foreground/30"
                          }`}
                        />
                      </button>
                    ))}
                  </div>
                </div>

                {/* Formality Level */}
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                      Formality Level
                    </Label>
                    <div className="flex items-center gap-2">
                      <div className={`w-3 h-3 rounded-full ${FORMALITY_COLORS[formData.formalityLevel[0] - 1]}`} />
                      <Badge variant="outline" className="border-border/50">
                        {formData.formalityLevel[0]} - {FORMALITY_LABELS[formData.formalityLevel[0] - 1]}
                      </Badge>
                    </div>
                  </div>
                  {isEditing && (
                    <>
                      <Slider
                        value={formData.formalityLevel}
                        onValueChange={(value) => setFormData({ ...formData, formalityLevel: value })}
                        min={1}
                        max={5}
                        step={1}
                        className="py-4"
                      />
                      <div className="flex justify-between text-xs text-muted-foreground">
                        <span>1 - Home</span>
                        <span>5 - Formal</span>
                      </div>
                    </>
                  )}
                </div>
              </CardContent>
            </Card>

            {/* Wear Today / Change Button */}
            {!isEditing && (
              todayHistory ? (
                <AlertDialog>
                  <AlertDialogTrigger asChild>
                    <Button className="w-full" size="lg" variant="outline">
                      <X className="w-4 h-4 mr-2" />
                      Change Today's Cody
                    </Button>
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>Change today's outfit?</AlertDialogTitle>
                      <AlertDialogDescription>
                        This will remove this outfit from today's calendar. You can select a different outfit for today.
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel>Cancel</AlertDialogCancel>
                      <AlertDialogAction onClick={handleChangeToday}>
                        Change
                      </AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              ) : (
                <Button className="w-full" size="lg" onClick={handleWearToday} disabled={checkingHistory}>
                  <Calendar className="w-4 h-4 mr-2" />
                  Wear This Outfit Today
                </Button>
              )
            )}
          </div>
        </div>
      </main>
    </div>
  )
}
