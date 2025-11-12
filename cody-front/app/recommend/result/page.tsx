"use client"

import { Suspense, useState, useEffect } from "react"
import { useRouter, useSearchParams } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { ArrowLeft, Star, Calendar, Sparkles, RefreshCw, Loader2 } from "lucide-react"
import { api, type Outfit } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const FORMALITY_COLORS = [
  "bg-[oklch(var(--formality-1))]",
  "bg-[oklch(var(--formality-2))]",
  "bg-[oklch(var(--formality-3))]",
  "bg-[oklch(var(--formality-4))]",
  "bg-[oklch(var(--formality-5))]",
]

const FORMALITY_LABELS = ["Home", "Neighborhood", "Outing", "Work", "Formal"]

function RecommendResultContent() {
  const router = useRouter()
  const { toast } = useToast()
  const searchParams = useSearchParams()
  const [recommendedOutfits, setRecommendedOutfits] = useState<Outfit[]>([])
  const [currentIndex, setCurrentIndex] = useState(0)
  const [isLoading, setIsLoading] = useState(true)
  const [isRecommending, setIsRecommending] = useState(false)

  // Get filter params from URL
  const minRating = searchParams.get("minRating") || "3"
  const minFormality = searchParams.get("minFormality") || "3"
  const excludeRecent = searchParams.get("excludeRecent") === "true"
  const requiredItemId = searchParams.get("requiredItemId")

  // Fetch recommended outfits from backend
  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        setIsLoading(true)
        const outfits = await api.recommendOutfits({
          minRating: Number(minRating),
          minFormality: Number(minFormality),
          excludeRecent,
        })

        if (outfits.length === 0) {
          toast({
            title: "No outfits found",
            description: "Try adjusting your filters or create more outfits.",
            variant: "destructive",
          })
          router.push("/recommend")
          return
        }

        setRecommendedOutfits(outfits)
      } catch (err) {
        console.error('Failed to fetch recommendations:', err)
        toast({
          title: "Error",
          description: "Failed to load recommendations. Please try again.",
          variant: "destructive",
        })
        router.push("/recommend")
      } finally {
        setIsLoading(false)
      }
    }

    fetchRecommendations()
  }, [minRating, minFormality, excludeRecent, router, toast])

  const handleRecommendAgain = () => {
    setIsRecommending(true)
    // Cycle to next outfit in the list
    setTimeout(() => {
      const nextIndex = (currentIndex + 1) % recommendedOutfits.length
      setCurrentIndex(nextIndex)
      setIsRecommending(false)
    }, 500)
  }

  const handleSelectOutfit = async () => {
    const outfit = recommendedOutfits[currentIndex]
    if (!outfit) return

    try {
      const today = new Date().toISOString().split('T')[0]
      await api.recordOutfitWorn(outfit.id, today)

      toast({
        title: "Success!",
        description: "Outfit recorded for today",
      })
      router.push("/calendar")
    } catch (error) {
      console.error('Failed to record outfit:', error)
      toast({
        title: "Error",
        description: "Failed to record outfit. Please try again.",
        variant: "destructive",
      })
    }
  }

  const getLastWornText = (lastWornDate: string | null) => {
    if (!lastWornDate) return "Never worn"

    const lastWorn = new Date(lastWornDate)
    const today = new Date()
    const diffTime = Math.abs(today.getTime() - lastWorn.getTime())
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

    if (diffDays === 0) return "Worn today"
    if (diffDays === 1) return "Worn yesterday"
    if (diffDays < 7) return `Worn ${diffDays} days ago`
    if (diffDays < 14) return "Worn 1 week ago"
    if (diffDays < 30) return `Worn ${Math.floor(diffDays / 7)} weeks ago`
    return "Never worn recently"
  }

  // Show loading state
  if (isLoading || recommendedOutfits.length === 0) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-primary animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Finding the perfect outfit...</p>
        </div>
      </div>
    )
  }

  const recommendedOutfit = recommendedOutfits[currentIndex]

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
              <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center">
                <Sparkles className="w-4 h-4 text-accent" />
              </div>
              <h1 className="text-xl font-serif tracking-tight">Recommended Outfit</h1>
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-4xl">
        {/* Active Filters */}
        <div className="mb-8 flex flex-wrap gap-2">
          <Badge variant="secondary" className="text-xs font-normal">
            Rating ≥ {minRating} ★
          </Badge>
          <Badge variant="secondary" className="text-xs font-normal">
            Formality ≥ {minFormality}
          </Badge>
          {excludeRecent && (
            <Badge variant="secondary" className="text-xs font-normal">
              Excluding recent (2 days)
            </Badge>
          )}
          {requiredItemId && (
            <Badge variant="secondary" className="text-xs font-normal">
              Includes specific item
            </Badge>
          )}
        </div>

        <div className="grid md:grid-cols-2 gap-8">
          {/* Left: Outfit Display */}
          <Card className="border border-border/50">
            <CardContent className="p-8">
              <h2 className="text-2xl font-serif font-light mb-6">{recommendedOutfit.name || "Your Outfit"}</h2>

              {/* Items Grid */}
              <div className="grid grid-cols-2 gap-4 mb-8">
                {recommendedOutfit.outfitItems.map((outfitItem) => (
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

              {/* Rating */}
              <div className="flex items-center justify-center gap-1 mb-6">
                {Array.from({ length: 5 }).map((_, i) => (
                  <Star
                    key={i}
                    className={`w-6 h-6 ${
                      i < recommendedOutfit.rating ? "fill-primary text-primary" : "text-muted-foreground/20"
                    }`}
                  />
                ))}
              </div>

              {/* Formality Badge */}
              <div className="flex items-center justify-center gap-2 mb-6">
                <div className={`w-3 h-3 rounded-full ${FORMALITY_COLORS[recommendedOutfit.formalityLevel - 1]}`} />
                <Badge variant="outline" className="border-border/50">
                  Formality {recommendedOutfit.formalityLevel} - {FORMALITY_LABELS[recommendedOutfit.formalityLevel - 1]}
                </Badge>
              </div>

              {/* Last Worn */}
              <div className="flex items-center justify-center gap-2 text-sm text-muted-foreground mb-2">
                <Calendar className="w-4 h-4" />
                <span className="font-light">{getLastWornText(recommendedOutfit.lastWornDate)}</span>
              </div>

              <div className="text-center text-xs text-muted-foreground/60 mb-6">
                Worn {recommendedOutfit.wornCount} times total
              </div>

              {/* Memo */}
              {recommendedOutfit.memo && (
                <Card className="bg-muted/30 border-border/50">
                  <CardContent className="p-4">
                    <p className="text-sm font-light text-muted-foreground italic">
                      &quot;{recommendedOutfit.memo}&quot;
                    </p>
                  </CardContent>
                </Card>
              )}
            </CardContent>
          </Card>

          {/* Right: Actions */}
          <div className="space-y-6">
            <Card className="border border-border/50 bg-accent/5">
              <CardContent className="p-8 text-center">
                <div className="w-16 h-16 rounded-full bg-accent/20 flex items-center justify-center mx-auto mb-6">
                  <Sparkles className="w-8 h-8 text-accent" />
                </div>
                <h3 className="text-xl font-serif font-light mb-3">This outfit is perfect for you!</h3>
                <p className="text-muted-foreground text-sm font-light mb-8 leading-relaxed">
                  Based on your preferences and wearing history, we think this outfit will work great for today.
                </p>
                <Button className="w-full" size="lg" onClick={handleSelectOutfit}>
                  Select This Outfit
                </Button>
              </CardContent>
            </Card>

            <Button
              variant="outline"
              className="w-full border-border/50"
              size="lg"
              onClick={handleRecommendAgain}
              disabled={isRecommending || recommendedOutfits.length <= 1}
            >
              {isRecommending ? (
                <>
                  <RefreshCw className="w-4 h-4 mr-2 animate-spin" />
                  Loading...
                </>
              ) : (
                <>
                  <RefreshCw className="w-4 h-4 mr-2" />
                  Recommend Again {recommendedOutfits.length > 1 && `(${currentIndex + 1}/${recommendedOutfits.length})`}
                </>
              )}
            </Button>

            <Button variant="ghost" className="w-full" asChild>
              <Link href="/recommend">Adjust Filters</Link>
            </Button>

            <Card className="border border-border/50">
              <CardContent className="p-6">
                <h4 className="text-sm font-light text-muted-foreground uppercase tracking-wider mb-4">
                  Quick Actions
                </h4>
                <div className="space-y-3">
                  <Button variant="outline" size="sm" className="w-full justify-start" asChild>
                    <Link href={`/outfits/${recommendedOutfit.id}`}>View Outfit Details</Link>
                  </Button>
                  <Button variant="outline" size="sm" className="w-full justify-start" asChild>
                    <Link href="/calendar">View Calendar</Link>
                  </Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </main>
    </div>
  )
}

export default function RecommendResultPage() {
  return (
    <Suspense fallback={
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Sparkles className="w-12 h-12 text-primary animate-pulse mx-auto mb-4" />
          <p className="text-muted-foreground">Loading recommendation...</p>
        </div>
      </div>
    }>
      <RecommendResultContent />
    </Suspense>
  )
}
