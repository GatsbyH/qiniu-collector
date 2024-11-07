-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 106.54.234.202
-- 生成日期： 2024-11-07 15:21:54
-- 服务器版本： 8.4.3
-- PHP 版本： 8.2.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `qiniu-collector`
--

-- --------------------------------------------------------

--
-- 表的结构 `file_detail`
--

CREATE TABLE `file_detail` (
  `id` bigint NOT NULL COMMENT '文件id',
  `url` varchar(512) NOT NULL COMMENT '文件访问地址',
  `size` bigint DEFAULT NULL COMMENT '文件大小，单位字节',
  `filename` varchar(256) DEFAULT NULL COMMENT '文件名称',
  `original_filename` varchar(256) DEFAULT NULL COMMENT '原始文件名',
  `base_path` varchar(256) DEFAULT NULL COMMENT '基础存储路径',
  `path` varchar(256) DEFAULT NULL COMMENT '存储路径',
  `ext` varchar(32) DEFAULT NULL COMMENT '文件扩展名',
  `content_type` varchar(128) DEFAULT NULL COMMENT 'MIME类型',
  `platform` varchar(32) DEFAULT NULL COMMENT '存储平台',
  `th_url` varchar(512) DEFAULT NULL COMMENT '缩略图访问路径',
  `th_filename` varchar(256) DEFAULT NULL COMMENT '缩略图名称',
  `th_size` bigint DEFAULT NULL COMMENT '缩略图大小，单位字节',
  `th_content_type` varchar(128) DEFAULT NULL COMMENT '缩略图MIME类型',
  `object_id` varchar(32) DEFAULT NULL COMMENT '文件所属对象id',
  `object_type` varchar(32) DEFAULT NULL COMMENT '文件所属对象类型，例如用户头像，评价图片',
  `metadata` text COMMENT '文件元数据',
  `user_metadata` text COMMENT '文件用户元数据',
  `th_metadata` text COMMENT '缩略图元数据',
  `th_user_metadata` text COMMENT '缩略图用户元数据',
  `attr` text COMMENT '附加属性',
  `file_acl` varchar(32) DEFAULT NULL COMMENT '文件ACL',
  `th_file_acl` varchar(32) DEFAULT NULL COMMENT '缩略图文件ACL',
  `hash_info` text COMMENT '哈希信息',
  `upload_id` varchar(128) DEFAULT NULL COMMENT '上传ID，仅在手动分片上传时使用',
  `upload_status` int DEFAULT NULL COMMENT '上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '修改人',
  `deleted` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='文件记录表' ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `scheduled_tasks`
--

CREATE TABLE `scheduled_tasks` (
  `id` int NOT NULL COMMENT '主键ID，自动增长',
  `field` varchar(255) NOT NULL COMMENT '领域',
  `status` varchar(50) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态：PENDING(待执行), RUNNING(执行中), COMPLETED(已完成), FAILED(失败)',
  `last_execution_time` datetime DEFAULT NULL COMMENT '上次执行时间',
  `next_execution_time` datetime DEFAULT NULL COMMENT '下次执行时间',
  `last_failure_message` text COMMENT '上次失败信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `deleted` char(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务调度表';

--
-- 转存表中的数据 `scheduled_tasks`
--

INSERT INTO `scheduled_tasks` (`id`, `field`, `status`, `last_execution_time`, `next_execution_time`, `last_failure_message`, `create_time`, `update_time`, `create_by`, `update_by`, `deleted`) VALUES
(86, 'deeplearning', 'COMPLETED', '2024-11-05 07:06:46', NULL, NULL, '2024-11-05 06:27:57', '2024-11-05 07:06:46', NULL, NULL, '0'),
(87, 'frontend', 'COMPLETED', '2024-11-05 09:17:57', NULL, NULL, '2024-11-05 07:33:21', '2024-11-05 09:17:57', NULL, NULL, '0'),
(88, 'backend', 'COMPLETED', '2024-11-05 09:19:17', NULL, NULL, '2024-11-05 08:06:16', '2024-11-05 09:19:17', NULL, NULL, '0'),
(89, 'machine learning', 'COMPLETED', '2024-11-05 09:17:38', NULL, NULL, '2024-11-05 08:34:52', '2024-11-05 09:17:38', NULL, NULL, '0'),
(95, '低代码', 'COMPLETED', '2024-11-05 10:06:12', NULL, NULL, '2024-11-05 09:32:33', '2024-11-05 10:06:12', NULL, NULL, '0'),
(97, 'goplus', 'COMPLETED', '2024-11-05 11:35:09', NULL, NULL, '2024-11-05 11:34:31', '2024-11-05 11:35:09', NULL, NULL, '0'),
(98, 'java', 'COMPLETED', '2024-11-05 13:41:30', NULL, NULL, '2024-11-05 11:36:48', '2024-11-05 13:41:30', NULL, NULL, '0'),
(99, 'vue', 'COMPLETED', '2024-11-05 14:17:02', NULL, NULL, '2024-11-05 13:42:32', '2024-11-05 14:17:02', NULL, NULL, '0'),
(101, 'lowcode', 'COMPLETED', '2024-11-05 15:18:56', NULL, NULL, '2024-11-05 15:09:36', '2024-11-05 15:18:56', NULL, NULL, '0'),
(102, 'react', 'COMPLETED', '2024-11-05 16:06:46', NULL, NULL, '2024-11-05 15:33:31', '2024-11-05 16:06:46', NULL, NULL, '0'),
(103, 'big-data', 'COMPLETED', '2024-11-06 16:08:43', NULL, NULL, '2024-11-06 15:34:31', '2024-11-06 16:08:43', NULL, NULL, '0'),
(104, 'python', 'COMPLETED', '2024-11-07 13:47:11', NULL, NULL, '2024-11-07 10:21:40', '2024-11-07 13:47:11', NULL, NULL, '0');

-- --------------------------------------------------------

--
-- 表的结构 `sensitive_word`
--

CREATE TABLE `sensitive_word` (
  `id` int UNSIGNED NOT NULL COMMENT '自增ID',
  `word_type` varchar(8) NOT NULL COMMENT '类型；allow-允许，deny-拒绝',
  `word` varchar(32) NOT NULL COMMENT '敏感词',
  `is_valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效；0无效、1有效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `sys_dict_data`
--

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='字典数据表';

--
-- 转存表中的数据 `sys_dict_data`
--

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `remark`) VALUES
(1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '性别男'),
(2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '性别女'),
(3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '性别未知'),
(4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '显示菜单'),
(5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '隐藏菜单'),
(6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '正常状态'),
(7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '停用状态'),
(8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '正常状态'),
(9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '停用状态'),
(10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '默认分组'),
(11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '系统分组'),
(12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '系统默认是'),
(13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '系统默认否'),
(14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '通知'),
(15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '公告'),
(16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '正常状态'),
(17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '关闭状态'),
(18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '其他操作'),
(19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '新增操作'),
(20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '修改操作'),
(21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '删除操作'),
(22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '授权操作'),
(23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '导出操作'),
(24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '导入操作'),
(25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '强退操作'),
(26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '生成操作'),
(27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '清空操作'),
(28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '正常状态'),
(29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '停用状态'),
(100, 0, '锻炼情况', '0', 'photo_type', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:39:52', 'admin', '2024-10-15 13:07:11', '0', NULL),
(101, 0, '灯光明灭', '1', 'photo_type', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:40:03', 'admin', '2024-10-15 13:07:28', '0', NULL),
(102, 0, '部门一', '0', 'dept_name', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:48:49', '', NULL, '0', NULL),
(103, 0, '部门二', '1', 'dept_name', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:49:00', '', NULL, '0', NULL),
(104, 0, '操场', '0', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:51:50', 'admin', '2024-10-15 13:05:11', '0', NULL),
(105, 0, '教学楼', '1', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-07 13:51:57', 'admin', '2024-10-15 13:05:24', '0', NULL),
(106, 0, '网球场', '2', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-15 13:05:46', '', NULL, '0', NULL),
(107, 0, '图书馆', '3', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-15 13:06:26', '', NULL, '0', NULL),
(108, 0, '篮球场', '4', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-15 13:06:40', '', NULL, '0', NULL),
(109, 0, '自习情况', '2', 'photo_type', NULL, 'default', 'N', '0', 'admin', '2024-10-15 13:07:49', '', NULL, '0', NULL),
(110, 0, '排球场', '5', 'photo_position', NULL, 'default', 'N', '0', 'admin', '2024-10-15 13:08:57', '', NULL, '0', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_dict_type`
--

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='字典类型表';

--
-- 转存表中的数据 `sys_dict_type`
--

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `remark`) VALUES
(1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '用户性别列表'),
(2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '菜单状态列表'),
(3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '系统开关列表'),
(4, '任务状态', 'sys_job_status', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '任务状态列表'),
(5, '任务分组', 'sys_job_group', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '任务分组列表'),
(6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '系统是否列表'),
(7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '通知类型列表'),
(8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '通知状态列表'),
(9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '操作类型列表'),
(10, '系统状态', 'sys_common_status', '0', 'admin', '2024-09-29 06:46:10', '', NULL, '0', '登录状态列表'),
(100, '照片类型', 'photo_type', '0', 'admin', '2024-10-07 13:38:12', 'admin', '2024-10-07 13:38:43', '0', NULL),
(101, '部门名称', 'dept_name', '0', 'admin', '2024-10-07 13:48:27', 'admin', '2024-10-07 13:48:30', '0', NULL),
(102, '图片位置', 'photo_position', '0', 'admin', '2024-10-07 13:51:33', '', NULL, '0', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_menu`
--

CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';

--
-- 转存表中的数据 `sys_menu`
--

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', '', 'admin', '2024-05-15 05:32:00', '', NULL, '系统管理目录'),
(2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '1', '0', '', 'monitor', '', 'admin', '2024-05-15 05:32:00', '', NULL, '系统监控目录'),
(3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '1', '0', '', 'tool', '', 'admin', '2024-05-15 05:32:00', '', NULL, '系统工具目录'),
(100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', '', 'admin', '2024-05-15 05:32:00', '', NULL, '用户管理菜单'),
(101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', '', 'admin', '2024-05-15 05:32:00', '', NULL, '角色管理菜单'),
(102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', '', 'admin', '2024-05-15 05:32:00', '', NULL, '菜单管理菜单'),
(103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '1', '0', 'system:dept:list', 'tree', '', 'admin', '2024-05-15 05:32:00', '', NULL, '部门管理菜单'),
(104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '1', '0', 'system:post:list', 'post', '', 'admin', '2024-05-15 05:32:00', '', NULL, '岗位管理菜单'),
(105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '1', '0', 'system:dict:list', 'dict', '', 'admin', '2024-05-15 05:32:00', '', NULL, '字典管理菜单'),
(106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '1', '0', 'system:config:list', 'edit', '', 'admin', '2024-05-15 05:32:00', '', NULL, '参数设置菜单'),
(107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '1', '0', 'system:notice:list', 'message', '', 'admin', '2024-05-15 05:32:00', '', NULL, '通知公告菜单'),
(108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '1', '0', '', 'log', '', 'admin', '2024-05-15 05:32:00', '', NULL, '日志管理菜单'),
(109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '1', '0', 'monitor:online:list', 'online', '', 'admin', '2024-05-15 05:32:00', '', NULL, '在线用户菜单'),
(110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '1', '0', 'monitor:job:list', 'job', '', 'admin', '2024-05-15 05:32:00', '', NULL, '定时任务菜单'),
(111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '1', '0', 'monitor:druid:list', 'druid', '', 'admin', '2024-05-15 05:32:00', '', NULL, '数据监控菜单'),
(112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '1', '0', 'monitor:server:list', 'server', '', 'admin', '2024-05-15 05:32:00', '', NULL, '服务监控菜单'),
(113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '1', '0', 'monitor:cache:list', 'redis', '', 'admin', '2024-05-15 05:32:00', '', NULL, '缓存监控菜单'),
(114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '1', '0', 'monitor:cache:list', 'redis-list', '', 'admin', '2024-05-15 05:32:00', '', NULL, '缓存列表菜单'),
(115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '1', '0', 'tool:build:list', 'build', '', 'admin', '2024-05-15 05:32:00', '', NULL, '表单构建菜单'),
(116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '1', '0', 'tool:gen:list', 'code', '', 'admin', '2024-05-15 05:32:00', '', NULL, '代码生成菜单'),
(117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '1', '0', 'tool:swagger:list', 'swagger', '', 'admin', '2024-05-15 05:32:00', '', NULL, '系统接口菜单'),
(500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '1', '0', 'monitor:operlog:list', 'form', '', 'admin', '2024-05-15 05:32:00', '', NULL, '操作日志菜单'),
(501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '1', '0', 'monitor:logininfor:list', 'logininfor', '', 'admin', '2024-05-15 05:32:00', '', NULL, '登录日志菜单'),
(1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', '', 'admin', '2024-05-15 05:32:00', '', NULL, ''),
(2002, '论文信息查询', 2001, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'paper:papers:query', '#', '', 'admin', '2024-05-15 06:45:27', '', NULL, ''),
(2003, '论文信息新增', 2001, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'paper:papers:add', '#', '', 'admin', '2024-05-15 06:45:27', '', NULL, ''),
(2004, '论文信息修改', 2001, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'paper:papers:edit', '#', '', 'admin', '2024-05-15 06:45:27', '', NULL, ''),
(2005, '论文信息删除', 2001, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'paper:papers:remove', '#', '', 'admin', '2024-05-15 06:45:27', '', NULL, ''),
(2006, '论文信息导出', 2001, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'paper:papers:export', '#', '', 'admin', '2024-05-15 06:45:27', '', NULL, ''),
(2008, 'GitHub数据管理', 0, 10, 'github', NULL, NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'documentation', '0', NULL, '2024-10-30 10:45:44', '', NULL, ''),
(2010, '领域搜索', 2008, 20, 'tasks', 'github/tasks/index', NULL, NULL, 1, 0, 'C', '0', '0', 'github:tasks:list', 'documentation', '0', NULL, '2024-10-30 23:49:31', NULL, '2024-10-31 10:24:20', '');

-- --------------------------------------------------------

--
-- 表的结构 `sys_role`
--

CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '逻辑删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='角色信息表';

--
-- 转存表中的数据 `sys_role`
--

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-09-29 06:46:09', '', NULL, '超级管理员'),
(2, '普通角色', 'common', 2, '2', 0, 0, '0', '0', 'admin', '2024-09-29 06:46:09', NULL, '2024-10-31 10:31:46', '普通角色'),
(100, '测试12', 'test12', 0, '1', 1, 1, '0', '1', NULL, '2024-10-18 20:50:11', NULL, '2024-10-19 20:06:34', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_role_menu`
--

CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';

--
-- 转存表中的数据 `sys_role_menu`
--

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(2, 100),
(2, 101),
(2, 102),
(2, 103),
(2, 104),
(2, 105),
(2, 106),
(2, 107),
(2, 108),
(2, 109),
(2, 110),
(2, 111),
(2, 112),
(2, 113),
(2, 114),
(2, 115),
(2, 116),
(2, 117),
(2, 500),
(2, 501),
(2, 2008),
(2, 2010);

-- --------------------------------------------------------

--
-- 表的结构 `sys_user`
--

CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '上传人',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '逻辑删除',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

--
-- 转存表中的数据 `sys_user`
--

INSERT INTO `sys_user` (`user_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `status`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `remark`) VALUES
(1, 101, 'zhangsan', '张三', '00', 'zhangsan@example.com', '13800138000', '0', 'http://example.com/avatar1.jpg', 'password123', '0', '192.168.1.1', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', 'admin', '2024-01-01 12:00:00', '0', '无'),
(2, 102, 'lisi', '李四', '00', 'lisi@example.com', '13800138001', '1', 'http://example.com/avatar2.jpg', 'password456', '0', '192.168.1.2', '2024-01-02 13:00:00', 'admin', '2024-01-02 13:00:00', 'admin', '2024-01-02 13:00:00', '0', '无'),
(3, 103, 'wangwu', '王五', '00', 'wangwu@example.com', '13800138002', '2', 'http://example.com/avatar3.jpg', 'password789', '1', '192.168.1.3', '2024-01-03 14:00:00', 'admin', '2024-01-03 14:00:00', NULL, '2024-10-18 13:52:41', '0', '无'),
(4, 104, 'zhaoliu', '赵六', '00', 'zhaoliu@example.com', '13800138003', '0', 'http://example.com/avatar4.jpg', 'password101', '0', '192.168.1.4', '2024-01-04 15:00:00', 'admin', '2024-01-04 15:00:00', NULL, '2024-10-18 11:30:32', '0', '无'),
(5, 105, 'zhouqi', '周七', '00', 'zhouqi@example.com', '13800138004', '1', 'http://example.com/avatar5.jpg', '123456', '0', '192.168.1.5', '2024-01-05 16:00:00', 'admin', '2024-01-05 16:00:00', NULL, '2024-10-18 13:42:29', '0', '无'),
(8, NULL, 'gatsby', 'gatsby', '00', '', '', '0', '', '123456', '0', '', NULL, NULL, '2024-10-17 21:42:22', NULL, NULL, '1', NULL),
(10, NULL, '11', '11', '00', '', '', '0', '', '111111', '0', '', NULL, NULL, '2024-10-19 20:29:49', NULL, NULL, '1', NULL),
(11, NULL, 'test', 'test', '00', '', '', '0', '', '123456', '0', '', NULL, NULL, '2024-10-31 10:32:15', NULL, NULL, '0', NULL),
(12, NULL, 'test2', 'test2', '00', '', '', '0', '', '123456', '0', '', NULL, NULL, '2024-10-31 03:50:54', NULL, NULL, '0', NULL),
(13, NULL, 'test3', 'test3', '00', '', '', '0', '', '123456', '0', '', NULL, NULL, '2024-10-31 03:51:09', NULL, NULL, '0', NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_user_role`
--

CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';

--
-- 转存表中的数据 `sys_user_role`
--

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(1, 2),
(4, 2),
(5, 2),
(11, 2),
(12, 2),
(13, 2);

--
-- 转储表的索引
--

--
-- 表的索引 `scheduled_tasks`
--
ALTER TABLE `scheduled_tasks`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `field` (`field`);

--
-- 表的索引 `sensitive_word`
--
ALTER TABLE `sensitive_word`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  ADD PRIMARY KEY (`dict_code`);

--
-- 表的索引 `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  ADD PRIMARY KEY (`dict_id`),
  ADD UNIQUE KEY `dict_type` (`dict_type`);

--
-- 表的索引 `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menu_id`);

--
-- 表的索引 `sys_role`
--
ALTER TABLE `sys_role`
  ADD PRIMARY KEY (`role_id`);

--
-- 表的索引 `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`role_id`,`menu_id`);

--
-- 表的索引 `sys_user`
--
ALTER TABLE `sys_user`
  ADD PRIMARY KEY (`user_id`);

--
-- 表的索引 `sys_user_role`
--
ALTER TABLE `sys_user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `scheduled_tasks`
--
ALTER TABLE `scheduled_tasks`
  MODIFY `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID，自动增长', AUTO_INCREMENT=105;

--
-- 使用表AUTO_INCREMENT `sensitive_word`
--
ALTER TABLE `sensitive_word`
  MODIFY `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID', AUTO_INCREMENT=5;

--
-- 使用表AUTO_INCREMENT `sys_dict_data`
--
ALTER TABLE `sys_dict_data`
  MODIFY `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码', AUTO_INCREMENT=111;

--
-- 使用表AUTO_INCREMENT `sys_dict_type`
--
ALTER TABLE `sys_dict_type`
  MODIFY `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键', AUTO_INCREMENT=103;

--
-- 使用表AUTO_INCREMENT `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID', AUTO_INCREMENT=2011;

--
-- 使用表AUTO_INCREMENT `sys_role`
--
ALTER TABLE `sys_role`
  MODIFY `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID', AUTO_INCREMENT=101;

--
-- 使用表AUTO_INCREMENT `sys_user`
--
ALTER TABLE `sys_user`
  MODIFY `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID', AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
