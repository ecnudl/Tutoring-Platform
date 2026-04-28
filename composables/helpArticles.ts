/**
 * 帮助文章集中配置。
 * slug 作为 URL 路径：/help/{slug}
 * configKey 对应 sys_config 表的 key，admin 填值后 admin 内容覆盖默认。
 *
 * category 决定右侧栏显示哪组相关链接：
 * - 'student'（我是学员）
 * - 'tutor'  （我是教员）
 * - 'common' （通用）
 */

export type HelpSection = {
  title: string
  intro?: string
  list?: string[]
  qa?: { q: string; a: string }[]
}

export type HelpArticle = {
  title: string
  configKey: string
  category: 'student' | 'tutor' | 'common'
  lead?: string
  steps?: { title: string; desc: string }[]
  sections?: HelpSection[]
  priceTable?: { headers: string[]; rows: string[][] }
  ctaText?: string
  actions?: { label: string; href: string; primary?: boolean }[]
}

export const HELP_ARTICLES: Record<string, HelpArticle> = {
  'request-process': {
    title: '请家教流程',
    configKey: 'helpRequestProcessHtml',
    category: 'student',
    lead: '591家教网提供免费、快捷、专业的家教匹配服务。只需 5 步，即可找到合适的家教老师。',
    steps: [
      { title: '发布家教需求', desc: '登录后点击"请家教"入口，填写辅导科目、年级、上课时间、地点、预算等信息；也可直接拨打客服热线由专员登记。' },
      { title: '平台智能匹配', desc: '24 小时内推荐 3-5 位合适的教员（含简历、授课经验、评价）供您挑选。' },
      { title: '电话/微信沟通', desc: '直接与推荐教员电话或微信沟通，了解对方教学风格、时间安排，确认是否合适。' },
      { title: '免费试讲', desc: '首次试讲 30 分钟免费，家长可现场观摩；不满意随时换人，不收任何费用。' },
      { title: '确认合作，开始上课', desc: '签订《家教辅导协议》，约定上课频率、课时数、课时费；教员按约上门或在线授课。' }
    ],
    sections: [
      {
        title: '服务保障',
        list: [
          '实名认证：所有教员均需上传学生证 / 教师资格证，平台审核通过方可接单。',
          '售后保障：家长不满意可<strong>免费换教员</strong>直到满意；中介费一次性、规则公开，无隐藏费用。',
          '免费换教员：首次正式上课 7 天内不满意可免费更换。',
          '全程可反馈：家长可随时通过客服或平台反馈教学情况。'
        ]
      },
      {
        title: '常见问题',
        qa: [
          { q: '试讲真的免费吗？', a: '首次试讲 30 分钟免费。部分特殊科目（如艺术类、乐器、海归外教）可能收取少量试讲费，具体与教员协商。' },
          { q: '课时费如何支付？', a: '由家长直接支付给教员，平台不经手。建议按课时或按周结算，避免纠纷。' },
          { q: '教员迟到或缺课怎么办？', a: '请第一时间拨打客服热线反馈，我们会协助处理或推荐新教员。' },
          { q: '可以指定教员性别/学校吗？', a: '可以。在"请家教"表单填写偏好，我们会优先匹配符合条件的教员。' }
        ]
      }
    ],
    ctaText: '有任何疑问，请拨打客服热线 <strong>13795420591</strong>',
    actions: [
      { label: '立即发布需求', href: '/qjj', primary: true },
      { label: '浏览教员库', href: '/jy' }
    ]
  },

  'find-tutor': {
    title: '怎样快速找到老师',
    configKey: 'helpFindTutorHtml',
    category: 'student',
    lead: '想让合适的教员尽快联系您？以下 <strong>4 个小技巧</strong> 能大幅提升匹配速度与质量。',
    steps: [
      { title: '详细填写需求信息', desc: '科目、年级、地点、时间、预算越具体，教员越容易判断是否匹配；空泛的描述会被忽略。' },
      { title: '手机号保持畅通', desc: '教员通常会在 24 小时内打电话了解详情。若错过来电请及时回拨。' },
      { title: '主动浏览教员库', desc: '不要只等推送。在 /jy 页面按条件筛选后，直接点"预约试讲"主动联系心仪教员。' },
      { title: '灵活对待试讲', desc: '多约 2-3 位教员试讲对比；试讲不满意不收费，敢于换人。' }
    ],
    sections: [
      {
        title: '影响匹配速度的因素',
        list: [
          '地区覆盖：市中心教员多，匹配更快；远郊需要耐心。',
          '科目稀缺度：IB/AP/小语种教员较少，建议提前一周发布需求。',
          '预算合理性：远低于市场价可能无人接单，参考"资费参考"页。',
          '上课时间：工作日晚、周末上午最抢手，尽早预约。'
        ]
      }
    ],
    ctaText: '如需专员协助匹配，拨打 <strong>13795420591</strong>',
    actions: [
      { label: '立即发布需求', href: '/qjj', primary: true },
      { label: '查看价格参考', href: '/zf' }
    ]
  },

  'request-faq': {
    title: '请家教常见问题',
    configKey: 'helpRequestFaqHtml',
    category: 'student',
    lead: '家长最关心的问题汇总，按主题分类。',
    sections: [
      {
        title: '关于平台',
        qa: [
          { q: '591家教网如何收费？', a: '家长：试讲匹配后一次性收 100 元中介费，不满意可<strong>免费换教员，直到满意为止</strong>。教员：上岗后中介费 = 首次课时费金额（如 2 小时 350 元 → 中介费 350 元）；只上 1 次课减半退还，2 次及以上不退。课时费由家长直接支付给教员，<strong>不经平台</strong>，平台不抽成。' },
          { q: '教员资质可靠吗？', a: '所有教员均须实名认证+上传学生证/教师资格证/学历证，审核通过后方可接单。可在 /jy 教员库查看认证标识。' },
          { q: '如果教员和我描述不符怎么办？', a: '首次试讲 30 分钟免费，不满意可直接换人。7 天内可无理由更换教员。' }
        ]
      },
      {
        title: '关于价格',
        qa: [
          { q: '课时费是怎么定的？', a: '由教员根据自身条件（学历、经验、科目难度）定价，家长与教员协商确认。我们提供 /zf 价格参考页。' },
          { q: '在线辅导便宜吗？', a: '通常是面授价格的 8-9 折，具体与教员协商。' },
          { q: '艺术类、小语种是否更贵？', a: '是。艺术类约比文化课高 20%-50%；小语种参考大学/成人价格栏目。' }
        ]
      },
      {
        title: '关于上课',
        qa: [
          { q: '上课地点在哪里？', a: '可选上门（教员上您家）或在线（视频授课）；部分教员也可在指定场所见面。' },
          { q: '可以更改上课时间吗？', a: '可以。提前 24 小时与教员协商即可。频繁变动会影响教员的时间安排。' },
          { q: '如果孩子不喜欢教员怎么办？', a: '首次试讲发现不合适，直接反馈平台重新匹配。已正式签约的 7 天内可免费更换。' },
          { q: '如果对教员一直不满意怎么办？', a: '联系客服免费推荐其他教员，<strong>直到您满意为止</strong>，无需额外支付中介费。' }
        ]
      }
    ],
    actions: [
      { label: '立即发布需求', href: '/qjj', primary: true },
      { label: '联系客服', href: '/about/contact' }
    ]
  },

  'become-tutor': {
    title: '成为家教老师',
    configKey: 'helpBecomeTutorHtml',
    category: 'tutor',
    lead: '注册成为教员，开启您的家教之旅。<strong>审核通过 24 小时内</strong> 即可接单。',
    steps: [
      { title: '注册账号', desc: '点击右上角"注册"选择"我是教员"，填写手机号、密码、验证码。' },
      { title: '完善个人资料', desc: '上传真实照片、学历证明/学生证/教师资格证、填写教学经验和可授课科目。' },
      { title: '等待审核', desc: '平台在 1-3 个工作日内完成审核，通过后发短信通知，状态变为"已发布"。' },
      { title: '浏览需求、申请接单', desc: '登录个人中心 → 查看最新需求 → 主动申请或等待平台推荐。' },
      { title: '试讲确认 → 正式上课', desc: '与家长电话沟通 → 免费试讲 → 签订辅导协议 → 按时上课并收取课时费。' }
    ],
    sections: [
      {
        title: '成为优质教员的要点',
        list: [
          '完整资料：真实照片、完整学历和经验说明，有证书加分。',
          '快速响应：家长咨询 24 小时内回复，不错过订单窗口。',
          '按时守约：试讲和正式课程都不迟到；有事提前协商。',
          '积累好评：家长评价好，平台会优先推送您给后续家长。'
        ]
      },
      {
        title: '教员权益',
        list: [
          '课时费 100% 归己：平台不抽成课时费；中介费一次性等同首次课时费，长期无负担。',
          '接单自由：自主选择学员、区域、时间，无派单强制。',
          '认证标识：审核通过后获"已认证"徽章，家长更信任。',
          '活跃度曝光：保持更新和高响应率，获更多推荐位。'
        ]
      }
    ],
    ctaText: '准备好开始了吗？点击下方按钮立即注册，或拨打 <strong>13795420591</strong> 咨询。',
    actions: [
      { label: '立即注册教员', href: '/register/teacher', primary: true },
      { label: '查看收费标准', href: '/help/tutor-pricing' }
    ]
  },

  'tutor-faq': {
    title: '做家教常见问题',
    configKey: 'helpTutorFaqHtml',
    category: 'tutor',
    lead: '教员最关心的问题汇总。',
    sections: [
      {
        title: '注册与审核',
        qa: [
          { q: '注册需要费用吗？', a: '不需要。注册、申请、接单、收款全程 0 手续费。' },
          { q: '审核多久能通过？', a: '通常 1-3 个工作日。上传真实证书照片可大幅加快审核。' },
          { q: '为什么审核被驳回？', a: '常见原因：资料不完整、证件照模糊、真实姓名与证件不符。按提示补齐再提交即可。' }
        ]
      },
      {
        title: '接单与收款',
        qa: [
          { q: '如何获取更多订单？', a: '完善资料、及时响应家长咨询、定期更新可授课时间；高响应率的教员会被优先推荐。' },
          { q: '课时费如何收取？', a: '家长直接付给您，平台不经手。建议按课时或按周现结，避免积压。' },
          { q: '遇到家长拖欠怎么办？', a: '保留聊天记录和转账凭证，拨打客服热线 13795420591，我们会协助协调。' }
        ]
      },
      {
        title: '教学建议',
        qa: [
          { q: '第一次试讲讲什么？', a: '建议准备一套诊断题+示范讲解，了解学生水平后给出辅导方案；让家长感觉"有针对性"。' },
          { q: '如何让家长续约？', a: '定期反馈学生进步、月末写简短总结、主动建议下阶段目标。' }
        ]
      }
    ],
    actions: [
      { label: '查看最新需求', href: '/xy', primary: true },
      { label: '联系客服', href: '/about/contact' }
    ]
  },

  'tutor-pricing': {
    title: '做家教收费标准',
    configKey: 'helpTutorPricingHtml',
    category: 'tutor',
    lead: '以下为 <strong>各年级各类别教员</strong> 的课时费参考，实际由教员与家长协商。',
    priceTable: {
      headers: ['教员类型', '小学', '初中', '高中', '大学/成人'],
      rows: [
        ['大学生',   '80-120', '100-150', '150-200', '150-250'],
        ['研究生',   '100-150', '120-180', '180-250', '200-300'],
        ['专职教员', '120-180', '150-220', '200-300', '250-400'],
        ['在职教师', '150-250', '200-300', '300-500', '300-500']
      ]
    },
    sections: [
      {
        title: '定价参考要点',
        list: [
          '文化课参考以上表格；艺术类（钢琴、美术、书法）通常高于文化课 20%-50%。',
          '海归外教、小语种（日语、法语、德语）按大学/成人栏目参考。',
          '在线辅导一般为面授的 8-9 折。',
          '试讲 30 分钟免费，是当前行业惯例；有经验后定价不需迁就。',
          '好评达到 10 条以上、成单 20 次以上的教员可以适当上浮。'
        ]
      }
    ],
    ctaText: '详细价格表请访问 <strong><a href="/zf" style="color:inherit;text-decoration:underline">/zf 资费参考</a></strong>',
    actions: [
      { label: '注册成为教员', href: '/register/teacher', primary: true },
      { label: '查看需求列表', href: '/xy' }
    ]
  },

  'payment-method': {
    title: '收费方式',
    configKey: 'helpPaymentMethodHtml',
    category: 'common',
    lead: '591家教网中介费规则：家长 100 元 / 教员首单金额；上 1 次课减半，2 次起不退；不满意可<strong>免费换教员到满意</strong>。下方为课时费结算方式说明（与中介费分开）。',
    sections: [
      {
        title: '支付方式',
        list: [
          '现金结算：适合上门辅导，每次课结束后当场付款。',
          '微信/支付宝转账：最常见，建议备注"XX 月第 N 次课"。',
          '银行转账：长期合作可考虑按月转账。',
          '不建议：预付超过 1 个月的大额款项。'
        ]
      },
      {
        title: '结算频率建议',
        qa: [
          { q: '按课时结算？', a: '适合短期辅导或刚合作的教员家长。灵活，双方都放心。' },
          { q: '按周结算？', a: '适合稳定合作 1 个月以上。教员记账简单、家长也好安排。' },
          { q: '按月结算？', a: '适合 1 年以上长期合作，建立信任后可月结。不建议付超过一个月。' }
        ]
      },
      {
        title: '费用争议处理',
        list: [
          '保留聊天记录、转账凭证、课程记录。',
          '双方先尝试沟通协商。',
          '无法协商请拨打 13795420591，平台会协助调解。',
          '平台只作调解，不强制执行；最终以法律途径为准。'
        ]
      }
    ],
    actions: [
      { label: '发布家教需求', href: '/qjj', primary: true },
      { label: '联系客服', href: '/about/contact' }
    ]
  }
}

/**
 * 右侧栏相关链接（按分类分组）
 */
export const HELP_SIDE_LINKS: Record<string, { label: string; href: string }[]> = {
  student: [
    { label: '请家教流程',       href: '/help/request-process' },
    { label: '怎样快速找到老师', href: '/help/find-tutor' },
    { label: '请家教常见问题',   href: '/help/request-faq' },
    { label: '请家教收费标准',   href: '/zf' },
    { label: '收费方式',         href: '/help/payment-method' }
  ],
  tutor: [
    { label: '成为家教老师',     href: '/help/become-tutor' },
    { label: '做家教收费标准',   href: '/help/tutor-pricing' },
    { label: '做家教常见问题',   href: '/help/tutor-faq' },
    { label: '收费方式',         href: '/help/payment-method' },
    { label: '立即注册教员',     href: '/register/teacher' }
  ],
  common: [
    { label: '请家教流程',       href: '/help/request-process' },
    { label: '成为家教老师',     href: '/help/become-tutor' },
    { label: '收费方式',         href: '/help/payment-method' },
    { label: '联系我们',         href: '/about/contact' }
  ]
}
