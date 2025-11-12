"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Slider } from "@/components/ui/slider"
import { Badge } from "@/components/ui/badge"
import { ArrowLeft, Star, Check, Loader2 } from "lucide-react"
import { api, type Item } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const FORMALITY_LABELS = ["Home", "Neighborhood", "Outing", "Work", "Formal"]

export default function CreateOutfitPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [items, setItems] = useState<Item[]>([])
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [selectedItems, setSelectedItems] = useState<Set<number>>(new Set())
  const [outfitName, setOutfitName] = useState("")
  const [rating, setRating] = useState(3)
  const [formalityLevel, setFormalityLevel] = useState([3])
  const [activeCategory, setActiveCategory] = useState("TOPS")

  // Fetch items from backend
  useEffect(() => {
    const fetchItems = async () => {
      try {
        setLoading(true)
        const data = await api.getItems()
        setItems(data)
      } catch (err) {
        console.error('Failed to fetch items:', err)
        toast({
          title: "Error",
          description: "Failed to load items.",
          variant: "destructive",
        })
      } finally {
        setLoading(false)
      }
    }

    fetchItems()
  }, [toast])

  const toggleItemSelection = (itemId: number) => {
    setSelectedItems((prev) => {
      const newSet = new Set(prev)
      if (newSet.has(itemId)) {
        newSet.delete(itemId)
      } else {
        newSet.add(itemId)
      }
      return newSet
    })
  }

  const itemsByCategory = items.reduce((acc, item) => {
    if (!acc[item.category]) {
      acc[item.category] = []
    }
    acc[item.category].push(item)
    return acc
  }, {} as Record<string, Item[]>)

  const categories = Object.keys(itemsByCategory)

  const handleSave = async () => {
    if (selectedItems.size === 0) {
      toast({
        title: "No Items Selected",
        description: "Please select at least one item for the outfit.",
        variant: "destructive",
      })
      return
    }

    setSaving(true)
    try {
      await api.createOutfit({
        name: outfitName || "New Outfit",
        rating,
        formalityLevel: formalityLevel[0],
        itemIds: Array.from(selectedItems),
      })

      toast({
        title: "Success!",
        description: "Outfit created successfully",
      })
      router.push("/outfits")
    } catch (error) {
      console.error('Failed to create outfit:', error)
      toast({
        title: "Error",
        description: "Failed to create outfit. Please try again.",
        variant: "destructive",
      })
    } finally {
      setSaving(false)
    }
  }

  const selectedItemsList = Array.from(selectedItems).map(id => items.find(item => item.id === id)).filter(Boolean) as Item[]
  const hasSelectedItems = selectedItems.size > 0

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
              <h1 className="text-xl font-serif tracking-tight">Create Outfit</h1>
            </div>
            <Button onClick={handleSave} disabled={!hasSelectedItems || saving}>
              {saving && <Loader2 className="w-4 h-4 mr-2 animate-spin" />}
              {saving ? "Saving..." : "Save Outfit"}
            </Button>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-6xl">
        {loading ? (
          <div className="text-center py-24">
            <Loader2 className="w-8 h-8 text-muted-foreground/50 animate-spin mx-auto mb-4" />
            <p className="text-muted-foreground">Loading items...</p>
          </div>
        ) : (
          <div className="grid lg:grid-cols-2 gap-8">
            {/* Left: Item Selection */}
            <div className="space-y-6">
              <Card className="border border-border/50">
                <CardContent className="p-6">
                  <h2 className="text-lg font-serif font-light mb-6">Select Items</h2>
                  <Tabs value={activeCategory} onValueChange={setActiveCategory}>
                    <TabsList className="grid grid-cols-4 lg:grid-cols-5 mb-6">
                      {categories.slice(0, 5).map((category) => (
                        <TabsTrigger key={category} value={category}>
                          {category}
                        </TabsTrigger>
                      ))}
                    </TabsList>

                    {categories.map((category) => (
                      <TabsContent key={category} value={category} className="mt-0">
                        {itemsByCategory[category].length === 0 ? (
                          <div className="text-center py-12">
                            <p className="text-muted-foreground text-sm">No items in this category</p>
                            <Button variant="ghost" size="sm" className="mt-4" asChild>
                              <Link href="/wardrobe/add">Add Item</Link>
                            </Button>
                          </div>
                        ) : (
                          <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
                            {itemsByCategory[category].map((item) => {
                              const isSelected = selectedItems.has(item.id)
                              return (
                                <div
                                  key={item.id}
                                  className={`relative cursor-pointer group ${
                                    isSelected ? "ring-2 ring-primary rounded-lg" : ""
                                  }`}
                                  onClick={() => toggleItemSelection(item.id)}
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
                                      <div className="p-3">
                                        <p className="text-sm font-light truncate">{item.name}</p>
                                      </div>
                                    </CardContent>
                                  </Card>
                                  {isSelected && (
                                    <div className="absolute top-2 right-2 w-8 h-8 rounded-full bg-primary flex items-center justify-center">
                                      <Check className="w-5 h-5 text-primary-foreground" />
                                    </div>
                                  )}
                                </div>
                              )
                            })}
                          </div>
                        )}
                      </TabsContent>
                    ))}
                  </Tabs>
                </CardContent>
              </Card>
            </div>

            {/* Right: Preview & Details */}
            <div className="space-y-6">
              {/* Preview */}
              <Card className="border border-border/50">
                <CardContent className="p-6">
                  <h2 className="text-lg font-serif font-light mb-6">Outfit Preview</h2>
                  {!hasSelectedItems ? (
                    <div className="text-center py-12 text-muted-foreground text-sm">
                      Select items from the left to preview your outfit
                    </div>
                  ) : (
                    <div className="grid grid-cols-2 gap-4">
                      {selectedItemsList.map((item) => (
                        <div key={item.id} className="space-y-2">
                          <Badge variant="secondary" className="text-xs font-normal">
                            {item.category}
                          </Badge>
                          <div className="aspect-square overflow-hidden rounded-lg bg-muted/30 border border-border/50">
                            <img
                              src={item.imageUrl || "/placeholder.svg"}
                              alt={item.name}
                              className="w-full h-full object-cover"
                            />
                          </div>
                          <p className="text-xs font-light text-center">{item.name}</p>
                        </div>
                      ))}
                    </div>
                  )}
                </CardContent>
              </Card>

              {/* Rating & Details */}
              <Card className="border border-border/50">
                <CardContent className="p-6 space-y-6">
                  <h2 className="text-lg font-serif font-light">Outfit Details</h2>

                  {/* Outfit Name */}
                  <div className="space-y-3">
                    <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                      Outfit Name (Optional)
                    </Label>
                    <Input
                      placeholder="e.g., Office Casual"
                      className="border-border/50"
                      value={outfitName}
                      onChange={(e) => setOutfitName(e.target.value)}
                    />
                  </div>

                  {/* Rating */}
                  <div className="space-y-3">
                    <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                      How much do you like this outfit?
                    </Label>
                    <div className="flex items-center gap-2">
                      {[1, 2, 3, 4, 5].map((value) => (
                        <button
                          key={value}
                          onClick={() => setRating(value)}
                          className="transition-colors"
                          type="button"
                        >
                          <Star
                            className={`w-8 h-8 ${
                              value <= rating ? "fill-primary text-primary" : "text-muted-foreground/30"
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
                      <Badge variant="outline" className="border-border/50">
                        {formalityLevel[0]} - {FORMALITY_LABELS[formalityLevel[0] - 1]}
                      </Badge>
                    </div>
                    <Slider
                      value={formalityLevel}
                      onValueChange={setFormalityLevel}
                      min={1}
                      max={5}
                      step={1}
                      className="py-4"
                    />
                    <div className="flex justify-between text-xs text-muted-foreground">
                      <span>1 - Home</span>
                      <span>5 - Formal</span>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        )}
      </main>
    </div>
  )
}
