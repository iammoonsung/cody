"use client"

export const runtime = 'edge';

import { use, useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
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
import { Badge } from "@/components/ui/badge"
import { ArrowLeft, Pencil, Trash2, Upload, X, Loader2 } from "lucide-react"
import { api, type Item } from "@/lib/api"
import { useToast } from "@/hooks/use-toast"

const CATEGORIES = ["TOPS", "BOTTOMS", "SHOES", "OUTERWEAR", "ACCESSORIES"]
const SEASONS = ["All", "SPRING", "SUMMER", "FALL", "WINTER"]
const COLORS = ["White", "Black", "Grey", "Navy", "Blue", "Beige", "Brown", "Red", "Pink", "Green", "Yellow", "Multi"]

export default function ItemDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = use(params)  // Next.js 15+ params unwrapping
  const router = useRouter()
  const { toast } = useToast()
  const [item, setItem] = useState<Item | null>(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [isEditing, setIsEditing] = useState(false)
  const [imagePreview, setImagePreview] = useState<string | null>(null)
  const [formData, setFormData] = useState({
    name: "",
    category: "",
    color: "",
    season: "",
  })

  // Fetch item data
  useEffect(() => {
    const fetchItem = async () => {
      try {
        setLoading(true)
        const data = await api.getItem(Number(resolvedParams.id))
        setItem(data)
        setImagePreview(data.imageUrl)
        setFormData({
          name: data.name,
          category: data.category,
          color: data.color,
          season: data.season || "All",
        })
      } catch (err) {
        console.error('Failed to fetch item:', err)
        toast({
          title: "Error",
          description: "Failed to load item details.",
          variant: "destructive",
        })
        router.push("/wardrobe")
      } finally {
        setLoading(false)
      }
    }

    fetchItem()
  }, [resolvedParams.id, router, toast])

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
      const updatedItem = await api.updateItem(Number(resolvedParams.id), {
        category: formData.category,
        name: formData.name,
        imageUrl: imagePreview,
        color: formData.color,
        season: formData.season === "All" ? undefined : formData.season,
      })

      setItem(updatedItem)
      setIsEditing(false)
      toast({
        title: "Success!",
        description: "Item updated successfully",
      })
    } catch (error) {
      console.error('Failed to update item:', error)
      toast({
        title: "Error",
        description: "Failed to update item. Please try again.",
        variant: "destructive",
      })
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async () => {
    try {
      await api.deleteItem(Number(resolvedParams.id))
      toast({
        title: "Success!",
        description: "Item deleted successfully",
      })
      router.push("/wardrobe")
    } catch (error) {
      console.error('Failed to delete item:', error)
      toast({
        title: "Error",
        description: "Failed to delete item. Please try again.",
        variant: "destructive",
      })
    }
  }

  const handleCancel = () => {
    if (item) {
      setFormData({
        name: item.name,
        category: item.category,
        color: item.color,
        season: item.season || "All",
      })
      setImagePreview(item.imageUrl)
    }
    setIsEditing(false)
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="w-12 h-12 text-primary animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Loading item...</p>
        </div>
      </div>
    )
  }

  if (!item) {
    return null
  }

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
              <h1 className="text-xl font-serif tracking-tight">Item Details</h1>
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
                          This action cannot be undone. This will permanently delete this item from your wardrobe.
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

      <main className="container mx-auto px-6 py-12 max-w-2xl">
        <Card className="border border-border/50">
          <CardContent className="p-8 space-y-8">
            {/* Image Display/Upload */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Item Photo
              </Label>
              {!isEditing ? (
                <div className="w-full h-80 border border-border/50 rounded-lg overflow-hidden bg-muted/10">
                  <img
                    src={imagePreview || "/placeholder.svg"}
                    alt={formData.name}
                    className="w-full h-full object-cover"
                  />
                </div>
              ) : !imagePreview ? (
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
                Category
              </Label>
              {!isEditing ? (
                <div>
                  <Badge variant="secondary" className="text-sm font-normal border-border/50">
                    {formData.category}
                  </Badge>
                </div>
              ) : (
                <Select value={formData.category} onValueChange={(value) => setFormData({ ...formData, category: value })}>
                  <SelectTrigger className="border-border/50">
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
              )}
            </div>

            {/* Item Name */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Item Name
              </Label>
              {!isEditing ? (
                <p className="text-lg font-light">{formData.name || "—"}</p>
              ) : (
                <Input
                  placeholder="e.g., Blue Oxford Shirt"
                  className="border-border/50"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                />
              )}
            </div>

            {/* Color */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Color
              </Label>
              {!isEditing ? (
                <div>
                  {formData.color ? (
                    <Badge variant="outline" className="text-sm font-normal border-border/50">
                      {formData.color}
                    </Badge>
                  ) : (
                    <p className="text-muted-foreground">—</p>
                  )}
                </div>
              ) : (
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
              )}
            </div>

            {/* Season */}
            <div className="space-y-3">
              <Label className="text-sm font-light text-muted-foreground uppercase tracking-wider">
                Season
              </Label>
              {!isEditing ? (
                <div>
                  {formData.season && formData.season !== "All" ? (
                    <Badge variant="outline" className="text-sm font-normal border-border/50">
                      {formData.season}
                    </Badge>
                  ) : (
                    <p className="text-muted-foreground">All Seasons</p>
                  )}
                </div>
              ) : (
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
              )}
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  )
}
