"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Plus, Search, Shirt, ArrowLeft, Filter, Loader2 } from "lucide-react"
import { api, type Item } from "@/lib/api"

const CATEGORIES = ["All", "TOPS", "BOTTOMS", "OUTERWEAR", "SHOES", "ACCESSORIES"]

export default function WardrobePage() {
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedCategory, setSelectedCategory] = useState("All")
  const [items, setItems] = useState<Item[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Fetch items from backend
  useEffect(() => {
    const fetchItems = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await api.getItems()
        setItems(data)
      } catch (err) {
        console.error('Failed to fetch items:', err)
        setError('Failed to load items. Please try again.')
      } finally {
        setLoading(false)
      }
    }

    fetchItems()
  }, [])

  const filteredItems = items.filter((item) => {
    const matchesSearch = item.name.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesCategory = selectedCategory === "All" || item.category === selectedCategory
    return matchesSearch && matchesCategory
  })

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
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
                <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center">
                  <Shirt className="w-4 h-4 text-primary" />
                </div>
                <h1 className="text-xl font-serif tracking-tight">My Wardrobe</h1>
              </div>
            </div>
            <Button variant="ghost" size="sm" asChild>
              <Link href="/wardrobe/add">
                <Plus className="w-4 h-4 mr-2" />
                Add Item
              </Link>
            </Button>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12">
        {/* Filters */}
        <div className="mb-12 space-y-6">
          <div className="flex flex-col md:flex-row gap-4 max-w-3xl">
            <div className="relative flex-1">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search your wardrobe..."
                className="pl-11 border-border/50 bg-background"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <Select value={selectedCategory} onValueChange={setSelectedCategory}>
              <SelectTrigger className="w-full md:w-48 border-border/50">
                <Filter className="w-4 h-4 mr-2" />
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {CATEGORIES.map((category) => (
                  <SelectItem key={category} value={category}>
                    {category}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
              {filteredItems.length} {filteredItems.length === 1 ? "item" : "items"}
            </p>
            <div className="flex gap-2">
              {CATEGORIES.slice(1).map((category) => (
                <Badge
                  key={category}
                  variant={selectedCategory === category ? "default" : "outline"}
                  className="cursor-pointer border-border/50"
                  onClick={() => setSelectedCategory(category)}
                >
                  {category}
                </Badge>
              ))}
            </div>
          </div>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="text-center py-24">
            <Loader2 className="w-8 h-8 text-muted-foreground/50 animate-spin mx-auto mb-4" />
            <p className="text-muted-foreground">Loading your wardrobe...</p>
          </div>
        )}

        {/* Error State */}
        {error && !loading && (
          <div className="text-center py-24">
            <div className="w-16 h-16 rounded-full bg-red-50 flex items-center justify-center mx-auto mb-6">
              <Shirt className="w-8 h-8 text-red-500" />
            </div>
            <h3 className="text-lg font-serif font-light mb-3">Error Loading Items</h3>
            <p className="text-muted-foreground mb-8 leading-relaxed">{error}</p>
            <Button
              variant="ghost"
              onClick={() => window.location.reload()}
            >
              Retry
            </Button>
          </div>
        )}

        {/* Items Grid */}
        {!loading && !error && (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {filteredItems.map((item) => (
              <Link key={item.id} href={`/wardrobe/${item.id}`}>
                <Card className="group border border-border/50 hover:border-border transition-all duration-300 cursor-pointer">
                  <CardContent className="p-0">
                    <div className="aspect-square overflow-hidden rounded-t-lg bg-muted/30">
                      <img
                        src={item.imageUrl || "/placeholder.svg"}
                        alt={item.name}
                        className="w-full h-full object-cover group-hover:scale-[1.02] transition-transform duration-500"
                      />
                    </div>
                    <div className="p-4">
                      <h3 className="font-light mb-3 text-sm">{item.name}</h3>
                      <div className="flex items-center gap-2 flex-wrap">
                        <Badge variant="secondary" className="text-xs font-normal border-border/50">
                          {item.category}
                        </Badge>
                        {item.season && (
                          <Badge variant="outline" className="text-xs font-normal border-border/50">
                            {item.season}
                          </Badge>
                        )}
                        <Badge variant="outline" className="text-xs font-normal border-border/50">
                          {item.color}
                        </Badge>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        )}

        {/* Empty State */}
        {!loading && !error && filteredItems.length === 0 && (
          <div className="text-center py-24">
            <div className="w-16 h-16 rounded-full bg-muted/30 flex items-center justify-center mx-auto mb-6">
              <Shirt className="w-8 h-8 text-muted-foreground/50" />
            </div>
            <h3 className="text-lg font-serif font-light mb-3">No items found</h3>
            <p className="text-muted-foreground mb-8 leading-relaxed">
              {items.length === 0 ? 'Start building your wardrobe' : 'Try adjusting your search or filters'}
            </p>
            {items.length === 0 ? (
              <Button asChild>
                <Link href="/wardrobe/add">
                  <Plus className="w-4 h-4 mr-2" />
                  Add Your First Item
                </Link>
              </Button>
            ) : (
              <Button
                variant="ghost"
                onClick={() => {
                  setSearchQuery("")
                  setSelectedCategory("All")
                }}
              >
                Clear Filters
              </Button>
            )}
          </div>
        )}
      </main>
    </div>
  )
}
