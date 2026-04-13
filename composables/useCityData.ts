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
  ],
  6: [ // 福州
    { id: 601, name: '鼓楼区', pinyin: 'gulou' },
    { id: 602, name: '台江区', pinyin: 'taijiang' },
    { id: 603, name: '仓山区', pinyin: 'cangshan' },
    { id: 604, name: '马尾区', pinyin: 'mawei' },
    { id: 605, name: '晋安区', pinyin: 'jinan' },
    { id: 606, name: '长乐区', pinyin: 'changle' },
    { id: 607, name: '闽侯县', pinyin: 'minhou' },
    { id: 608, name: '福清市', pinyin: 'fuqing' }
  ],
  7: [ // 济南
    { id: 701, name: '历下区', pinyin: 'lixia' },
    { id: 702, name: '市中区', pinyin: 'shizhong' },
    { id: 703, name: '槐荫区', pinyin: 'huaiyin' },
    { id: 704, name: '天桥区', pinyin: 'tianqiao' },
    { id: 705, name: '历城区', pinyin: 'licheng' },
    { id: 706, name: '长清区', pinyin: 'changqing' },
    { id: 707, name: '章丘区', pinyin: 'zhangqiu' },
    { id: 708, name: '济阳区', pinyin: 'jiyang' },
    { id: 709, name: '莱芜区', pinyin: 'laiwu' },
    { id: 710, name: '高新区', pinyin: 'gaoxin' }
  ],
  9: [ // 北京
    { id: 901, name: "东城区", pinyin: "dongcheng" },
    { id: 902, name: "西城区", pinyin: "xicheng" },
    { id: 903, name: "朝阳区", pinyin: "chaoyang" },
    { id: 904, name: "丰台区", pinyin: "fengtai" },
    { id: 905, name: "石景山区", pinyin: "shijingshan" },
    { id: 906, name: "海淀区", pinyin: "haidian" },
    { id: 907, name: "门头沟区", pinyin: "mentougou" },
    { id: 908, name: "房山区", pinyin: "fangshan" },
    { id: 909, name: "通州区", pinyin: "tongzhou" },
    { id: 910, name: "顺义区", pinyin: "shunyi" },
    { id: 911, name: "昌平区", pinyin: "changping" },
    { id: 912, name: "大兴区", pinyin: "daxing" },
    { id: 913, name: "怀柔区", pinyin: "huairou" },
    { id: 914, name: "平谷区", pinyin: "pinggu" },
    { id: 915, name: "密云区", pinyin: "miyun" },
    { id: 916, name: "延庆区", pinyin: "yanqing" }
  ],
  10: [ // 天津
    { id: 1001, name: "和平区", pinyin: "heping" },
    { id: 1002, name: "河东区", pinyin: "hedong" },
    { id: 1003, name: "河西区", pinyin: "hexi" },
    { id: 1004, name: "南开区", pinyin: "nankai" },
    { id: 1005, name: "河北区", pinyin: "hebei" },
    { id: 1006, name: "红桥区", pinyin: "hongqiao" },
    { id: 1007, name: "东丽区", pinyin: "dongli" },
    { id: 1008, name: "西青区", pinyin: "xiqing" },
    { id: 1009, name: "津南区", pinyin: "jinnan" },
    { id: 1010, name: "北辰区", pinyin: "beichen" },
    { id: 1011, name: "武清区", pinyin: "wuqing" },
    { id: 1012, name: "宝坻区", pinyin: "baodi" },
    { id: 1013, name: "滨海新区", pinyin: "binhaixinqu" },
    { id: 1014, name: "宁河区", pinyin: "ninghe" },
    { id: 1015, name: "静海区", pinyin: "jinghai" },
    { id: 1016, name: "蓟州区", pinyin: "jizhou" }
  ],
  11: [ // 广州
    { id: 1101, name: "荔湾区", pinyin: "liwan" },
    { id: 1102, name: "越秀区", pinyin: "yuexiu" },
    { id: 1103, name: "海珠区", pinyin: "haizhu" },
    { id: 1104, name: "天河区", pinyin: "tianhe" },
    { id: 1105, name: "白云区", pinyin: "baiyun" },
    { id: 1106, name: "黄埔区", pinyin: "huangpu" },
    { id: 1107, name: "番禺区", pinyin: "panyu" },
    { id: 1108, name: "花都区", pinyin: "huadu" },
    { id: 1109, name: "南沙区", pinyin: "nansha" },
    { id: 1110, name: "从化区", pinyin: "conghua" },
    { id: 1111, name: "增城区", pinyin: "zengcheng" }
  ],
  12: [ // 武汉
    { id: 1201, name: "江岸区", pinyin: "jiangan" },
    { id: 1202, name: "江汉区", pinyin: "jianghan" },
    { id: 1203, name: "硚口区", pinyin: "qiaokou" },
    { id: 1204, name: "汉阳区", pinyin: "hanyang" },
    { id: 1205, name: "武昌区", pinyin: "wuchang" },
    { id: 1206, name: "青山区", pinyin: "qingshan" },
    { id: 1207, name: "洪山区", pinyin: "hongshan" },
    { id: 1208, name: "东西湖区", pinyin: "dongxihu" },
    { id: 1209, name: "蔡甸区", pinyin: "caidian" },
    { id: 1210, name: "江夏区", pinyin: "jiangxia" },
    { id: 1211, name: "黄陂区", pinyin: "huangpi" },
    { id: 1212, name: "新洲区", pinyin: "xinzhou" },
    { id: 1213, name: "经济技术开发区", pinyin: "jingji" },
    { id: 1214, name: "东湖高新区", pinyin: "donghu" }
  ],
  8: [ // 南昌
    { id: 801, name: '东湖区', pinyin: 'donghu' },
    { id: 802, name: '西湖区', pinyin: 'xihu' },
    { id: 803, name: '青云谱区', pinyin: 'qingyunpu' },
    { id: 804, name: '青山湖区', pinyin: 'qingshanhu' },
    { id: 805, name: '新建区', pinyin: 'xinjian' },
    { id: 806, name: '红谷滩区', pinyin: 'honggutan' },
    { id: 807, name: '南昌县', pinyin: 'nanchangxian' },
    { id: 808, name: '安义县', pinyin: 'anyi' },
    { id: 809, name: '进贤县', pinyin: 'jinxian' }
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
  ],
  6: [
    '福州大学', '福建师范大学', '福建农林大学', '福建医科大学',
    '福建中医药大学', '闽江学院', '福建工程学院', '福建江夏学院',
    '福建商学院', '福建警察学院', '阳光学院', '福州外语外贸学院'
  ],
  7: [
    '山东大学', '济南大学', '山东师范大学', '山东财经大学',
    '齐鲁工业大学', '山东建筑大学', '山东中医药大学', '山东政法学院',
    '山东艺术学院', '山东体育学院', '山东女子学院', '山东交通学院',
    '齐鲁师范学院', '山东英才学院'
  ],
  9: [
    "北京大学", "清华大学", "中国人民大学", "北京师范大学",
    "北京航空航天大学", "北京理工大学", "中国农业大学", "北京林业大学",
    "北京中医药大学", "中央财经大学", "对外经济贸易大学", "北京外国语大学",
    "中国传媒大学", "中央民族大学", "北京邮电大学", "北京交通大学",
    "中国地质大学(北京)", "北京化工大学", "北京工业大学", "首都师范大学",
    "首都医科大学", "北京语言大学", "中国政法大学", "中央音乐学院",
    "中央美术学院", "北京体育大学", "北京第二外国语学院", "中国科学院大学",
    "北京协和医学院", "中国音乐学院"
  ],
  10: [
    "南开大学", "天津大学", "天津师范大学", "天津医科大学",
    "天津工业大学", "天津理工大学", "天津财经大学", "天津外国语大学",
    "天津体育学院", "天津音乐学院", "天津美术学院", "天津中医药大学",
    "天津职业技术师范大学", "中国民航大学", "天津城建大学", "天津商业大学"
  ],
  11: [
    "中山大学", "华南理工大学", "暨南大学", "华南师范大学",
    "广州大学", "广东工业大学", "华南农业大学", "南方医科大学",
    "广州医科大学", "广州中医药大学", "广东外语外贸大学", "广州美术学院",
    "星海音乐学院", "广东财经大学", "广州体育学院", "广州航海学院",
    "广东技术师范大学", "仲恺农业工程学院"
  ],
  12: [
    "武汉大学", "华中科技大学", "武汉理工大学", "华中师范大学",
    "华中农业大学", "中国地质大学(武汉)", "中南财经政法大学", "武汉科技大学",
    "湖北大学", "武汉工程大学", "武汉纺织大学", "湖北工业大学",
    "江汉大学", "武汉体育学院", "湖北中医药大学", "武汉音乐学院",
    "中南民族大学", "武汉大学口腔医学院"
  ],
  8: [
    '南昌大学', '江西师范大学', '江西财经大学', '华东交通大学',
    '东华理工大学', '江西农业大学', '江西中医药大学', '南昌航空大学',
    '江西科技师范大学', '南昌工程学院', '江西警察学院', '豫章师范学院',
    '南昌理工学院', '江西应用科技学院'
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
