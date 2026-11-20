export type GameCategory = 'quiz' | 'debate' | 'interview'

export interface Game {
  id: number
  name: string
  description: string
  category: GameCategory
  screen: string
  icon: string
  color: string
}
