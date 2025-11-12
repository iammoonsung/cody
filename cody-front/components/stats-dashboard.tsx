"use client"

import { useState, useEffect } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { api } from "@/lib/api"

export function StatsDashboard() {
  const [stats, setStats] = useState({
    itemCount: 0,
    outfitCount: 0,
    recordedDays: 0,
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true)
        
        // 병렬로 데이터 가져오기
        const [items, outfits, histories] = await Promise.all([
          api.getItems(),
          api.getOutfits(),
          api.getHistories(),
        ])

        // 고유한 날짜 개수 계산 (기록된 일수)
        const uniqueDates = new Set(histories.map(h => h.wornDate))

        setStats({
          itemCount: items.length,
          outfitCount: outfits.length,
          recordedDays: uniqueDates.size,
        })
      } catch (error) {
        console.error('Failed to fetch stats:', error)
        // 에러 발생 시 0으로 유지
      } finally {
        setLoading(false)
      }
    }

    fetchStats()
  }, [])

  return (
    <Card className="border border-border/50">
      <CardContent className="p-12">
        <div className="grid grid-cols-3 gap-12 text-center">
          <div>
            <div className="text-4xl font-serif font-light text-foreground mb-2">
              {loading ? "..." : stats.itemCount}
            </div>
            <div className="text-sm text-muted-foreground uppercase tracking-wider">
              옷장 아이템
            </div>
          </div>
          <div>
            <div className="text-4xl font-serif font-light text-foreground mb-2">
              {loading ? "..." : stats.outfitCount}
            </div>
            <div className="text-sm text-muted-foreground uppercase tracking-wider">
              저장된 코디
            </div>
          </div>
          <div>
            <div className="text-4xl font-serif font-light text-foreground mb-2">
              {loading ? "..." : stats.recordedDays}
            </div>
            <div className="text-sm text-muted-foreground uppercase tracking-wider">
              기록된 일수
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

