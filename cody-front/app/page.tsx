import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Sparkles, Briefcase, Coffee, Calendar, Shirt, ChevronRight, SlidersHorizontal } from "lucide-react"
import { StatsDashboard } from "@/components/stats-dashboard"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      <main className="container mx-auto px-6">
        <div className="text-center py-20 max-w-3xl mx-auto">
          <h2 className="text-5xl md:text-6xl font-serif font-light mb-6 text-balance tracking-tight">
            오늘 뭐 입지?
          </h2>
          <p className="text-lg text-muted-foreground leading-relaxed text-pretty">
            내 옷장, 날씨, 일정에 맞춘 스타일 추천
          </p>
        </div>

        <div className="grid md:grid-cols-2 gap-8 max-w-4xl mx-auto mb-8">
          <Link href="/recommend/result?minRating=4&minFormality=4&excludeRecent=true">
            <Card className="border border-border/50 hover:border-primary/30 hover:shadow-sm transition-all duration-300 group cursor-pointer h-full">
              <CardContent className="p-10">
                <div className="w-12 h-12 rounded-full bg-primary/5 flex items-center justify-center mb-6 group-hover:bg-primary/10 transition-colors">
                  <Briefcase className="w-6 h-6 text-primary" />
                </div>
                <h3 className="text-2xl font-serif font-light mb-3 text-balance">업무 복장</h3>
                <p className="text-muted-foreground leading-relaxed mb-6">오피스를 위한 전문적인 룩</p>
                <Button variant="ghost" className="px-0 group-hover:gap-2 transition-all">
                  추천받기
                  <ChevronRight className="w-4 h-4 ml-1 group-hover:translate-x-1 transition-transform" />
                </Button>
              </CardContent>
            </Card>
          </Link>

          <Link href="/recommend/result?minRating=3&minFormality=3&excludeRecent=true">
            <Card className="border border-border/50 hover:border-accent/30 hover:shadow-sm transition-all duration-300 group cursor-pointer h-full">
              <CardContent className="p-10">
                <div className="w-12 h-12 rounded-full bg-accent/5 flex items-center justify-center mb-6 group-hover:bg-accent/10 transition-colors">
                  <Coffee className="w-6 h-6 text-accent" />
                </div>
                <h3 className="text-2xl font-serif font-light mb-3 text-balance">캐주얼 외출</h3>
                <p className="text-muted-foreground leading-relaxed mb-6">주말을 위한 편안한 스타일</p>
                <Button variant="ghost" className="px-0 group-hover:gap-2 transition-all">
                  추천받기
                  <ChevronRight className="w-4 h-4 ml-1 group-hover:translate-x-1 transition-transform" />
                </Button>
              </CardContent>
            </Card>
          </Link>
        </div>

        <div className="max-w-4xl mx-auto mb-24 text-center">
          <Button variant="outline" size="lg" className="border-border/50" asChild>
            <Link href="/recommend">
              <SlidersHorizontal className="w-4 h-4 mr-2" />
              Advanced Filter
            </Link>
          </Button>
        </div>

        <div className="grid md:grid-cols-3 gap-6 max-w-5xl mx-auto mb-24">
          <Link href="/wardrobe" className="group">
            <Card className="h-full border border-border/50 hover:border-border transition-all duration-300">
              <CardContent className="p-8">
                <Shirt className="w-8 h-8 text-foreground/60 mb-6 group-hover:text-foreground transition-colors" />
                <h3 className="text-lg font-light mb-2">내 옷장</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">옷 컬렉션 관리하기</p>
              </CardContent>
            </Card>
          </Link>

          <Link href="/outfits" className="group">
            <Card className="h-full border border-border/50 hover:border-border transition-all duration-300">
              <CardContent className="p-8">
                <Sparkles className="w-8 h-8 text-foreground/60 mb-6 group-hover:text-foreground transition-colors" />
                <h3 className="text-lg font-light mb-2">저장된 코디</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">큐레이션된 코디 조합</p>
              </CardContent>
            </Card>
          </Link>

          <Link href="/calendar" className="group">
            <Card className="h-full border border-border/50 hover:border-border transition-all duration-300">
              <CardContent className="p-8">
                <Calendar className="w-8 h-8 text-foreground/60 mb-6 group-hover:text-foreground transition-colors" />
                <h3 className="text-lg font-light mb-2">히스토리</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">매일의 코디 기록하기</p>
              </CardContent>
            </Card>
          </Link>
        </div>

        <div className="max-w-4xl mx-auto pb-24">
          <StatsDashboard />
        </div>
      </main>
    </div>
  )
}
