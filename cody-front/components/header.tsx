import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Shirt } from "lucide-react"

export function Header() {
  return (
    <header className="border-b border-border/50 bg-background/80 backdrop-blur-md sticky top-0 z-10">
      <div className="container mx-auto px-6 py-6">
        <div className="flex items-center justify-between">
          <Link href="/" className="flex items-center gap-3 group">
            <div className="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
              <Shirt className="w-4 h-4 text-primary" />
            </div>
            <h1 className="text-xl font-serif tracking-tight text-balance">오늘뭐입지?</h1>
          </Link>
          <nav className="flex items-center gap-1">
            <Button variant="ghost" size="sm" className="text-sm" asChild>
              <Link href="/wardrobe">옷장</Link>
            </Button>
            <Button variant="ghost" size="sm" className="text-sm" asChild>
              <Link href="/outfits">코디</Link>
            </Button>
            <Button variant="ghost" size="sm" className="text-sm" asChild>
              <Link href="/calendar">캘린더</Link>
            </Button>
          </nav>
        </div>
      </div>
    </header>
  )
}

