CREATE TABLE `t_commodity` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `standard` varchar(50) DEFAULT NULL COMMENT '商品规格',
  `temperature` varchar(50) DEFAULT NULL COMMENT '商品温度',
  `price` int(10) DEFAULT NULL COMMENT '商品价格',
  `description` varchar(200) DEFAULT NULL COMMENT '商品描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

