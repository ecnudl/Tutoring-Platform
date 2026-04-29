-- 加 北京 到 dict_city + 16 个区到 dict_district. 幂等.
INSERT IGNORE INTO `dict_city` (`id`, `status_id`, `city_name`, `city_pinyin`, `province_name`, `is_hot`, `sort`)
VALUES (10, 1, '北京', 'beijing', '北京市', 1, 2);

-- 北京 16 区
INSERT IGNORE INTO `dict_district` (`id`, `status_id`, `city_id`, `district_name`, `district_pinyin`, `sort`) VALUES
(101001, 1, 10, '东城区',  'dongcheng',  1),
(101002, 1, 10, '西城区',  'xicheng',    2),
(101003, 1, 10, '朝阳区',  'chaoyang',   3),
(101004, 1, 10, '海淀区',  'haidian',    4),
(101005, 1, 10, '丰台区',  'fengtai',    5),
(101006, 1, 10, '石景山区','shijingshan',6),
(101007, 1, 10, '门头沟区','mentougou',  7),
(101008, 1, 10, '房山区',  'fangshan',   8),
(101009, 1, 10, '通州区',  'tongzhou',   9),
(101010, 1, 10, '顺义区',  'shunyi',    10),
(101011, 1, 10, '昌平区',  'changping', 11),
(101012, 1, 10, '大兴区',  'daxing',    12),
(101013, 1, 10, '怀柔区',  'huairou',   13),
(101014, 1, 10, '平谷区',  'pinggu',    14),
(101015, 1, 10, '密云区',  'miyun',     15),
(101016, 1, 10, '延庆区',  'yanqing',   16);
