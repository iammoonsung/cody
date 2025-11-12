"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { ArrowLeft, Upload, X, Loader2 } from "lucide-react"
import { api } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const CATEGORIES = ["Tops", "Bottoms", "Shoes", "Outerwear", "Accessories"]
const SEASONS = ["All", "Spring", "Summer", "Fall", "Winter"]
const COLORS = ["White", "Black", "Grey", "Navy", "Blue", "Beige", "Brown", "Red", "Pink", "Green", "Yellow", "Multi"]

export default function AddItemPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [imagePreview, setImagePreview] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)
  const [formData, setFormData] = useState({
    name: "",
    category: "",
    color: "",
    season: "",
  })

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onloadend = () => {
        setImagePreview(reader.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleRemoveImage = () => {
    setImagePreview(null)
  }

  const handleSave = async () => {
    if (!formData.category || !imagePreview) {
      toast({
        title: "Missing Information",
        description: "Please fill in all required fields",
        variant: "destructive",
      })
      return
    }

    setSaving(true)

    try {
      // Map frontend values to backend format
      const categoryMap: Record<string, string> = {
        "Tops": "TOPS",
        "Bottoms": "BOTTOMS",
        "Shoes": "SHOES",
        "Outerwear": "OUTERWEAR",
        "Accessories": "ACCESSORIES"
      }

      const seasonMap: Record<string, string | undefined> = {
        "All": undefined,
        "Spring": "SPRING",
        "Summer": "SUMMER",
        "Fall": "FALL",
        "Winter": "WINTER"
      }

      const itemData = {
        category: categoryMap[formData.category] || formData.category.toUpperCase(),
        name: formData.name || `${formData.category} Item`,
        imageUrl: imagePreview, // In production, upload to cloud storage first
        color: formData.color || "Unknown",
        season: formData.season ? seasonMap[formData.season] : undefined,
      }

      await api.createItem(itemData)

      toast({
        title: "Success!",
        description: "Item added to your wardrobe",
      })

      router.push("/wardrobe")
    } catch (error) {
      console.error('Failed to save item:', error)
      toast({
        title: "Error",
        description: "Failed to save item. Please try again.",
        variant: "destructive",
      })
    } finally {
      setSaving(false)
    }
  }

  const isFormValid = formData.category && imagePreview

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b border-border/50 bg-background/80 backdrop-blur-md sticky top-0 z-10">
        <div className="container mx-auto px-6 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Button variant="ghost" size="icon" asChild>
                <Link href="/wardrobe">
                  <ArrowLeft className="w-4 h-4" />
                </Link>
              </Button>
              <h1 className="text-xl font-serif tracking-tight">Add Item</h1>
            </div>
            <Button onClick={handleSave} disabled={!isFormValid || saving}>
              {saving && <Loader2 className="w-4 h-4 mr-2 animate-spin" />}
              {saving ? "Saving..." : "Save"}
            </Button>
          </div>
        </div>
      </header>

      <main className="container mx-auto px-6 py-12 max-w-2xl">
        <Card className="border border-border/50">
          <CardContent className="p-8 space-y-8">
            {/* Image Upload */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Item Photo *
              </Label>
              {!imagePreview ? (
                <label
                  htmlFor="image-upload"
                  className="flex flex-col items-center justify-center w-full h-80 border-2 border-dashed border-border/50 rounded-lg cursor-pointer hover:border-primary/30 transition-colors bg-muted/10"
                >
                  <div className="flex flex-col items-center justify-center py-8">
                    <div className="w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                      <Upload className="w-8 h-8 text-primary" />
                    </div>
                    <p className="text-sm font-light text-muted-foreground mb-2">Click to upload image</p>
                    <p className="text-xs text-muted-foreground/60">PNG, JPG up to 10MB</p>
                  </div>
                  <input
                    id="image-upload"
                    type="file"
                    className="hidden"
                    accept="image/*"
                    onChange={handleImageUpload}
                  />
                </label>
              ) : (
                <div className="relative w-full h-80 border border-border/50 rounded-lg overflow-hidden">
                  <img src={imagePreview} alt="Preview" className="w-full h-full object-cover" />
                  <Button
                    variant="destructive"
                    size="icon"
                    className="absolute top-4 right-4"
                    onClick={handleRemoveImage}
                  >
                    <X className="w-4 h-4" />
                  </Button>
                </div>
              )}
            </div>

            {/* Category */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Category *
              </Label>
              <Select value={formData.category} onValueChange={(value) => setFormData({ ...formData, category: value })}>
                <SelectTrigger className="border-border/50">
                  <SelectValue placeholder="Select category" />
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

            {/* Item Name */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Item Name (Optional)
              </Label>
              <Input
                placeholder="e.g., Blue Oxford Shirt"
                className="border-border/50"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
            </div>

            {/* Color */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Color (Optional)
              </Label>
              <Select value={formData.color} onValueChange={(value) => setFormData({ ...formData, color: value })}>
                <SelectTrigger className="border-border/50">
                  <SelectValue placeholder="Select color" />
                </SelectTrigger>
                <SelectContent>
                  {COLORS.map((color) => (
                    <SelectItem key={color} value={color}>
                      {color}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Season */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Season (Optional)
              </Label>
              <Select value={formData.season} onValueChange={(value) => setFormData({ ...formData, season: value })}>
                <SelectTrigger className="border-border/50">
                  <SelectValue placeholder="Select season" />
                </SelectTrigger>
                <SelectContent>
                  {SEASONS.map((season) => (
                    <SelectItem key={season} value={season}>
                      {season}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  )
}
