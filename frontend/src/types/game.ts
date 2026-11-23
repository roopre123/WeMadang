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

export const categoryLabels: Record<GameCategory, string> = {
  quiz: '퀴즈형',
  debate: '토론형',
  interview: '면담형',
}

export const categoryTabs = [
  { value: '', label: '전체' },
  { value: 'quiz', label: categoryLabels.quiz },
  { value: 'debate', label: categoryLabels.debate },
  { value: 'interview', label: categoryLabels.interview },
]
