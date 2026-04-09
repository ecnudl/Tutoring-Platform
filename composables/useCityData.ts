import { ref, computed, watch } from 'vue'
import { useCityStore } from '~/stores/city'

// 各城市区域数据
const cityDistricts: Record<number, { id: number; name: string; pinyin: string }[]> = {
  1: [ // 上海
    { id: 101, name: '浦东新区', pinyin: 'pudong' },
    { id: 102, name: '徐汇区', pinyin: 'xuhui' },
    { id: 103, name: '长宁区', pinyin: 'changning' },
    { id: 104, name: '闵行区', pinyin: 'minhang' },
    { id: 105, name: '黄浦区', pinyin: 'huangpu' },
    { id: 106, name: '静安区', pinyin: 'jingan' },
    { id: 107, name: '虹口区', pinyin: 'hongkou' },
    { id: 108, name: '杨浦区', pinyin: 'yangpu' },
    { id: 109, name: '普陀区', pinyin: 'putuo' },
    { id: 110, name: '嘉定区', pinyin: 'jiading' },
    { id: 111, name: '金山区', pinyin: 'jinshan' },
    { id: 112, name: '青浦区', pinyin: 'qingpu' },
    { id: 113, name: '宝山区', pinyin: 'baoshan' },
    { id: 114, name: '松江区', pinyin: 'songjiang' },
    { id: 115, name: '奉贤区', pinyin: 'fengxian' },
    { id: 116, name: '崇明区', pinyin: 'chongming' }
  ],
  2: [ // 苏州
    { id: 201, name: '姑苏区', pinyin: 'gusu' },
    { id: 202, name: '虎丘区', pinyin: 'huqiu' },
    { id: 203, name: '吴中区', pinyin: 'wuzhong' },
    { id: 204, name: '相城区', pinyin: 'xiangcheng' },
    { id: 205, name: '吴江区', pinyin: 'wujiang' },
    { id: 206, name: '工业园区', pinyin: 'gongyeyuanqu' },
    { id: 207, name: '昆山市', pinyin: 'kunshan' },
    { id: 208, name: '常熟市', pinyin: 'changshu' },
    { id: 209, name: '张家港市', pinyin: 'zhangjiagang' },
    { id: 210, name: '太仓市', pinyin: 'taicang' }
  ],
  3: [ // 合肥
    { id: 301, name: '蜀山区', pinyin: 'shushan' },
    { id: 302, name: '庐阳区', pinyin: 'luyang' },
    { id: 303, name: '包河区', pinyin: 'baohe' },
    { id: 304, name: '瑶海区', pinyin: 'yaohai' },
    { id: 305, name: '高新区', pinyin: 'gaoxin' },
    { id: 306, name: '经开区', pinyin: 'jingkai' },
    { id: 307, name: '新站区', pinyin: 'xinzhan' },
    { id: 308, name: '肥东县', pinyin: 'feidong' },
    { id: 309, name: '肥西县', pinyin: 'feixi' }
  ],
  5: [ // 南京
    { id: 501, name: '玄武区', pinyin: 'xuanwu' },
    { id: 502, name: '秦淮区', pinyin: 'qinhuai' },
    { id: 503, name: '建邺区', pinyin: 'jianye' },
    { id: 504, name: '鼓楼区', pinyin: 'gulou' },
    { id: 505, name: '浦口区', pinyin: 'pukou' },
    { id: 506, name: '栖霞区', pinyin: 'qixia' },
    { id: 507, name: '雨花台区', pinyin: 'yuhuatai' },
    { id: 508, name: '江宁区', pinyin: 'jiangning' },
    { id: 509, name: '六合区', pinyin: 'liuhe' },
    { id: 510, name: '溧水区', pinyin: 'lishui' },
    { id: 511, name: '高淳区', pinyin: 'gaochun' }
  ],
  4: [ // 杭州
    { id: 401, name: '上城区', pinyin: 'shangcheng' },
    { id: 402, name: '拱墅区', pinyin: 'gongshu' },
    { id: 403, name: '西湖区', pinyin: 'xihu' },
    { id: 404, name: '滨江区', pinyin: 'binjiang' },
    { id: 405, name: '萧山区', pinyin: 'xiaoshan' },
    { id: 406, name: '余杭区', pinyin: 'yuhang' },
    { id: 407, name: '临平区', pinyin: 'linping' },
    { id: 408, name: '钱塘区', pinyin: 'qiantang' },
    { id: 409, name: '富阳区', pinyin: 'fuyang' },
    { id: 410, name: '临安区', pinyin: 'linan' }
  ]
}

// 各城市热门高校（用于筛选面板）
const cityUniversities: Record<number, string[]> = {
  1: [
    '复旦大学', '上海交通大学', '同济大学', '华东师范大学', '华东理工大学',
    '上海大学', '上海财经大学', '上海外国语大学', '东华大学', '上海理工大学',
    '上海师范大学', '上海海事大学', '上海对外经贸大学', '上海中医药大学',
    '上海海洋大学', '上海电力大学', '上海音乐学院', '上海戏剧学院',
    '上海体育大学', '上海工程技术大学', '上海应用技术大学', '上海立信会计金融学院',
    '上海第二工业大学', '上海电机学院', '上海商学院', '上海政法学院',
    '华东政法大学', '上海科技大学', '上海纽约大学'
  ],
  2: [
    '苏州大学', '西交利物浦大学', '苏州科技大学', '常熟理工学院',
    '昆山杜克大学', '苏州城市学院', '苏州职业大学', '苏州经贸职业技术学院',
    '苏州工业职业技术学院', '硅湖职业技术学院', '沙洲职业工学院',
    '江苏科技大学张家港校区', '南京大学苏州校区'
  ],
  3: [
    '中国科学技术大学', '合肥工业大学', '安徽大学', '安徽医科大学',
    '安徽农业大学', '合肥学院', '合肥师范学院', '安徽建筑大学',
    '安徽中医药大学', '安徽艺术学院', '合肥职业技术学院',
    '安徽新华学院', '安徽外国语学院', '安徽三联学院'
  ],
  5: [
    '南京大学', '东南大学', '南京航空航天大学', '南京理工大学',
    '河海大学', '南京师范大学', '南京邮电大学', '南京农业大学',
    '南京林业大学', '南京信息工程大学', '南京医科大学', '南京中医药大学',
    '南京财经大学', '南京审计大学', '南京工业大学', '南京工程学院',
    '南京艺术学院', '南京体育学院', '南京晓庄学院', '金陵科技学院',
    '中国药科大学', '南京传媒学院'
  ],
  4: [
    '浙江大学', '浙江工业大学', '杭州电子科技大学', '浙江工商大学',
    '中国美术学院', '浙江理工大学', '杭州师范大学', '浙江财经大学',
    '浙江中医药大学', '中国计量大学', '浙江科技大学', '浙江传媒学院',
    '浙江外国语学院', '杭州医学院', '浙大城市学院', '浙江音乐学院',
    '浙江树人学院', '杭州电子科技大学信息工程学院'
  ]
}

export function useCityData() {
  const cityStore = useCityStore()

  const districts = computed(() => {
    return cityDistricts[cityStore.cityId] || cityDistricts[1]
  })

  const districtNames = computed(() => {
    return districts.value.map(d => d.name)
  })

  const universities = computed(() => {
    return cityUniversities[cityStore.cityId] || cityUniversities[1]
  })

  return {
    districts,
    districtNames,
    universities,
    cityDistricts,
    cityUniversities
  }
}
