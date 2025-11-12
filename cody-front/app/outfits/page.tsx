"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Plus, ArrowLeft, Star, Sparkles, Loader2 } from "lucide-react"
import { api, type Outfit } from "@/lib/api"

const FORMALITY_COLORS = [
  "bg-[oklch(var(--formality-1))]",
  "bg-[oklch(var(--formality-2))]",
  "bg-[oklch(var(--formality-3))]",
  "bg-[oklch(var(--formality-4))]",
  "bg-[oklch(var(--formality-5))]",
]

export default function OutfitsPage() {
  const [selectedFormality, setSelectedFormality] = useState<number | null>(null)
  const [outfits, setOutfits] = useState<Outfit[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Fetch outfits from backend
  useEffect(() => {
    const fetchOutfits = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await api.getOutfits()
        setOutfits(data)
      } catch (err) {
        console.error('Failed to fetch outfits:', err)
        setError('Failed to load outfits. Please try again.')
      } finally {
        setLoading(false)
      }
    }

    fetchOutfits()
  }, [])

  const filteredOutfits = selectedFormality
    ? outfits.filter((outfit) => outfit.formalityLevel === selectedFormality)
    : outfits

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b border-border/50 bg-background/80 backdrop-blur-md sticky top-0 z-10">
        <div className="container mx-auto px-6 py-6">
          <div className="flex items-center justify-between">
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
                <h1 className="text-xl font-serif tracking-tight">Saved Outfits</h1>
              </div>
            </div>
            <Button variant="ghost" size="sm" asChild>
              <Link href="/outfits/create">
                <Plus className="w-4 h-4 mr-2" />
                Create Outfit
              </Link>
            </Button>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12">
        <div className="mb-12 max-w-3xl">
          <h3 className="text-sm font-light text-muted-foreground mb-4 uppercase tracking-wider">
            Filter by Formality Level
          </h3>
          <div className="flex gap-2 flex-wrap">
            <Button
              variant={selectedFormality === null ? "default" : "outline"}
              onClick={() => setSelectedFormality(null)}
              size="sm"
              className="border-border/50"
            >
              All
            </Button>
            {[1, 2, 3, 4, 5].map((level) => (
              <Button
                key={level}
                variant={selectedFormality === level ? "default" : "outline"}
                onClick={() => setSelectedFormality(level)}
                size="sm"
                className="flex items-center gap-2 border-border/50"
              >
                <div className={`w-2.5 h-2.5 rounded-full ${FORMALITY_COLORS[level - 1]}`} />
                Level {level}
              </Button>
            ))}
          </div>
          <p className="text-xs text-muted-foreground mt-3">1 = Very Casual • 5 = Very Formal</p>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="text-center py-24">
            <Loader2 className="w-8 h-8 text-muted-foreground/50 animate-spin mx-auto mb-4" />
            <p className="text-muted-foreground">Loading your outfits...</p>
          </div>
        )}

        {/* Error State */}
        {error && !loading && (
          <div className="text-center py-24">
            <div className="w-16 h-16 rounded-full bg-red-50 flex items-center justify-center mx-auto mb-6">
              <Sparkles className="w-8 h-8 text-red-500" />
            </div>
            <h3 className="text-lg font-serif font-light mb-3">Error Loading Outfits</h3>
            <p className="text-muted-foreground mb-8 leading-relaxed">{error}</p>
            <Button
              variant="ghost"
              onClick={() => window.location.reload()}
            >
              Retry
            </Button>
          </div>
        )}

        {/* Outfits Grid */}
        {!loading && !error && (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredOutfits.map((outfit) => (
              <Link key={outfit.id} href={`/outfits/${outfit.id}`}>
                <Card className="border border-border/50 hover:border-border transition-all duration-300 cursor-pointer">
                  <CardContent className="p-6">
                    {/* Outfit Preview */}
                    <div className="aspect-square bg-muted/30 rounded-lg mb-6 flex items-center justify-center overflow-hidden">
                      {outfit.outfitItems.length > 0 && outfit.outfitItems[0].item.imageUrl ? (
                        <img
                          src={outfit.outfitItems[0].item.imageUrl}
                          alt={outfit.name}
                          className="w-full h-full object-cover"
                        />
                      ) : (
                        <Sparkles className="w-12 h-12 text-muted-foreground/30" />
                      )}
                    </div>

                    {/* Outfit Info */}
                    <div className="space-y-4">
                      <div className="flex items-start justify-between">
                        <h3 className="font-serif font-light text-lg">{outfit.name}</h3>
                        <div className="flex items-center gap-0.5">
                          {Array.from({ length: 5 }).map((_, i) => (
                            <Star
                              key={i}
                              className={`w-3.5 h-3.5 ${
                                i < outfit.rating ? "fill-primary text-primary" : "text-muted-foreground/20"
                              }`}
                            />
                          ))}
                        </div>
                      </div>

                      {/* Formality Badge */}
                      <div className="flex items-center gap-2">
                        <div className={`w-3 h-3 rounded-full ${FORMALITY_COLORS[outfit.formalityLevel - 1]}`} />
                        <span className="text-sm text-muted-foreground font-light">Formality Level {outfit.formalityLevel}</span>
                      </div>

                      {/* Items List */}
                      <div className="flex flex-wrap gap-2">
                        {outfit.outfitItems.map((outfitItem) => (
                          <Badge key={outfitItem.id} variant="secondary" className="text-xs font-normal border-border/50">
                            {outfitItem.item.name}
                          </Badge>
                        ))}
                      </div>

                      {/* Stats */}
                      <div className="flex items-center justify-between text-sm text-muted-foreground pt-4 border-t border-border/50">
                        <span className="font-light">Worn {outfit.wornCount}x</span>
                        <span className="font-light">
                          {outfit.lastWornDate ? `Last: ${new Date(outfit.lastWornDate).toLocaleDateString()}` : 'Never worn'}
                        </span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        )}

        {/* Empty State */}
        {!loading && !error && filteredOutfits.length === 0 && (
          <div className="text-center py-24">
            <div className="w-16 h-16 rounded-full bg-muted/30 flex items-center justify-center mx-auto mb-6">
              <Sparkles className="w-8 h-8 text-muted-foreground/50" />
            </div>
            <h3 className="text-lg font-serif font-light mb-3">No outfits found</h3>
            <p className="text-muted-foreground mb-8 leading-relaxed">
              {outfits.length === 0 ? 'Start creating your outfits' : 'Try adjusting your filters or create a new outfit'}
            </p>
            {outfits.length === 0 ? (
              <Button asChild>
                <Link href="/outfits/create">
                  <Plus className="w-4 h-4 mr-2" />
                  Create Your First Outfit
                </Link>
              </Button>
            ) : (
              <Button variant="ghost" onClick={() => setSelectedFormality(null)}>
                Clear Filters
              </Button>
            )}
          </div>
        )}
      </main>
    </div>
  )
}
