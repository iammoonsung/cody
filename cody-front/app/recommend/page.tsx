"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Slider } from "@/components/ui/slider"
import { Switch } from "@/components/ui/switch"
import { Badge } from "@/components/ui/badge"
import { ArrowLeft, Star, Sparkles, X, Loader2 } from "lucide-react"
import { api, type Item } from "@/lib/api"

const FORMALITY_LABELS = ["Home", "Neighborhood", "Outing", "Work", "Formal"]

export default function RecommendPage() {
  const router = useRouter()
  const [minRating, setMinRating] = useState([3])
  const [minFormality, setMinFormality] = useState([3])
  const [excludeRecent, setExcludeRecent] = useState(true)
  const [selectedItem, setSelectedItem] = useState<Item | null>(null)
  const [showItemPicker, setShowItemPicker] = useState(false)
  const [items, setItems] = useState<Item[]>([])
  const [loading, setLoading] = useState(true)

  // Fetch items from backend
  useEffect(() => {
    const fetchItems = async () => {
      try {
        setLoading(true)
        const data = await api.getItems()
        setItems(data)
      } catch (err) {
        console.error('Failed to fetch items:', err)
      } finally {
        setLoading(false)
      }
    }

    fetchItems()
  }, [])

  const handleGetRecommendation = () => {
    // Build query params
    const params = new URLSearchParams({
      minRating: minRating[0].toString(),
      minFormality: minFormality[0].toString(),
      excludeRecent: excludeRecent.toString(),
    })

    if (selectedItem) {
      params.append("requiredItemId", selectedItem.id.toString())
    }

    // Navigate to recommendation result page
    router.push(`/recommend/result?${params.toString()}`)
  }

  const handleSelectItem = (item: Item) => {
    setSelectedItem(item)
    setShowItemPicker(false)
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-primary animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Loading items...</p>
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
              <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                <Sparkles className="w-4 h-4 text-primary" />
              </div>
              <h1 className="text-xl font-serif tracking-tight">Custom Recommendation</h1>
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-2xl">
        <Card className="border border-border/50">
          <CardContent className="p-8 space-y-8">
            {/* Rating Filter */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                  Minimum Rating
                </Label>
                <div className="flex items-center gap-1">
                  {Array.from({ length: minRating[0] }).map((_, i) => (
                    <Star key={i} className="w-4 h-4 fill-primary text-primary" />
                  ))}
                  {Array.from({ length: 5 - minRating[0] }).map((_, i) => (
                    <Star key={i} className="w-4 h-4 text-muted-foreground/30" />
                  ))}
                </div>
              </div>
              <Slider value={minRating} onValueChange={setMinRating} min={1} max={5} step={1} className="py-4" />
              <div className="flex justify-between text-xs text-muted-foreground">
                <span>1 Star</span>
                <span>5 Stars</span>
              </div>
            </div>

            {/* Formality Filter */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                  Minimum Formality
                </Label>
                <Badge variant="outline" className="border-border/50">
                  {minFormality[0]} - {FORMALITY_LABELS[minFormality[0] - 1]}
                </Badge>
              </div>
              <Slider value={minFormality} onValueChange={setMinFormality} min={1} max={5} step={1} className="py-4" />
              <div className="flex justify-between text-xs text-muted-foreground">
                <span>1 - Very Casual</span>
                <span>5 - Very Formal</span>
              </div>
            </div>

            {/* Item Filter */}
            <div className="space-y-4">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Must Include Item (Optional)
              </Label>
              {selectedItem ? (
                <Card className="border border-border/50 relative">
                  <CardContent className="p-4 flex items-center gap-4">
                    <div className="w-20 h-20 rounded-lg overflow-hidden bg-muted/30">
                      <img
                        src={selectedItem.imageUrl || "/placeholder.svg"}
                        alt={selectedItem.name}
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <div className="flex-1">
                      <p className="font-light">{selectedItem.name}</p>
                      <Badge variant="secondary" className="text-xs font-normal mt-1">
                        Required
                      </Badge>
                    </div>
                    <Button
                      variant="ghost"
                      size="icon"
                      className="absolute top-2 right-2"
                      onClick={() => setSelectedItem(null)}
                    >
                      <X className="w-4 h-4" />
                    </Button>
                  </CardContent>
                </Card>
              ) : (
                <Button
                  variant="outline"
                  className="w-full border-border/50 border-dashed"
                  onClick={() => setShowItemPicker(!showItemPicker)}
                >
                  Select Item
                </Button>
              )}

              {/* Item Picker */}
              {showItemPicker && !selectedItem && (
                <Card className="border border-border/50">
                  <CardContent className="p-4">
                    {items.length === 0 ? (
                      <div className="text-center py-8">
                        <p className="text-muted-foreground text-sm mb-4">No items available</p>
                        <Button variant="ghost" size="sm" asChild>
                          <Link href="/wardrobe/add">Add Items</Link>
                        </Button>
                      </div>
                    ) : (
                      <div className="grid grid-cols-3 gap-3 max-h-80 overflow-y-auto">
                        {items.map((item) => (
                          <div
                            key={item.id}
                            className="cursor-pointer group"
                            onClick={() => handleSelectItem(item)}
                          >
                            <Card className="border border-border/50 hover:border-primary/30 transition-all">
                              <CardContent className="p-0">
                                <div className="aspect-square overflow-hidden rounded-t-lg bg-muted/30">
                                  <img
                                    src={item.imageUrl || "/placeholder.svg"}
                                    alt={item.name}
                                    className="w-full h-full object-cover"
                                  />
                                </div>
                                <div className="p-2">
                                  <p className="text-xs font-light truncate">{item.name}</p>
                                </div>
                              </CardContent>
                            </Card>
                          </div>
                        ))}
                      </div>
                    )}
                  </CardContent>
                </Card>
              )}
            </div>

            {/* Recent Filter */}
            <div className="flex items-center justify-between py-4 border-t border-border/50">
              <div className="space-y-1">
                <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                  Exclude Recent Outfits
                </Label>
                <p className="text-xs text-muted-foreground/60">Don't show outfits worn in the last 2 days</p>
              </div>
              <Switch checked={excludeRecent} onCheckedChange={setExcludeRecent} />
            </div>
          </CardContent>
        </Card>

        {/* Get Recommendation Button */}
        <Button className="w-full mt-8" size="lg" onClick={handleGetRecommendation}>
          <Sparkles className="w-4 h-4 mr-2" />
          Get Recommendation
        </Button>
      </main>
    </div>
  )
}
