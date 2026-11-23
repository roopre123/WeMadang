// 페이지 번호는 최대 5개, 지금 페이지가 가운데 오게 이동 (없는 페이지는 만들지 않음)
export function pageWindow(page: number, totalPages: number): number[] {
  const last = Math.max(totalPages, 1)
  const start = Math.min(Math.max(page - 2, 1), Math.max(last - 4, 1))
  const end = Math.min(start + 4, last)
  return Array.from({ length: end - start + 1 }, (_, i) => start + i)
}
