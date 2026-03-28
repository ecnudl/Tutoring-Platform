import { defineStore } from 'pinia'
export const useDictStore = defineStore('dict', {
  state: () => ({ subjects: [] as any[], grades: [] as any[], tags: [] as any[] }),
  actions: {
    async fetchAll() {
      const { get } = useApi()
      try {
        const [s, g, t] = await Promise.all([get('/user/api/dict/subject/list'), get('/user/api/dict/grade/list'), get('/user/api/dict/tag/list')])
        this.subjects = s?.data || []; this.grades = g?.data || []; this.tags = t?.data || []
      } catch (e) { console.error(e) }
    }
  }
})
