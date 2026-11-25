import type { Game, GameCategory } from '@/types/game'

export type SearchParams = {
  category: string
  sort: string
  keyword: string
  page: number
}

// 서버가 보내 주는 게임 하나 (대문자)
interface ApiGame {
  id: number
  name: string
  description: string
  category: 'QUIZ' | 'DEBATE' | 'INTERVIEW'
  screen: string
  icon: string | null
  color: string
}

// 서버가 보내 주는 응답 전체
interface ApiGameList {
  games: ApiGame[]
  page: number
  totalPages: number
}


// 화면에 돌려줄 결과 (소문자로 바꾼 Game)
export interface GameList {
  games: Game[]
  page: number
  totalPages: number
}

export async function fetchGames({ category, sort, keyword, page }: SearchParams): Promise<GameList> {
  const params = new URLSearchParams()

  if (category !== '') {
    params.set('category', category.toUpperCase())
  }

  params.set('sort', sort.toUpperCase())

  if (keyword !== '') {
    params.set('keyword', keyword)
  }

  params.set('page', String(page))

  const res = await fetch('/api/games?' + params.toString(), { method: 'GET' })

  if (!res.ok) throw new Error('게임 목록을 불러오지 못했어요.')

  const data: ApiGameList = await res.json()

  const games: Game[] = data.games.map((game: ApiGame) => ({
    ...game,
    category: game.category.toLowerCase() as GameCategory,
    color: game.color.toLowerCase(),
    icon: game.icon ?? '',
  }))

  return {games, page: data.page, totalPages: data.totalPages}
}
