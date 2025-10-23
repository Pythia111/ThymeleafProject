package org.example.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;

@Service
public class DecisionTreeGameService {
    private Map<String, DecisionNode> decisionTree;
    private Map<String, Integer> keyNodeAges;
    private List<RandomEvent> randomEvents;
    private Random random = new Random();

    @PostConstruct
    public void init() {
        decisionTree = new HashMap<>();
        keyNodeAges = new HashMap<>();
        randomEvents = new ArrayList<>();
        buildKeyNodeAges();
        buildRandomEvents();
        buildDecisionTree();
    }

    private void buildKeyNodeAges() {
        // 重新设计关键节点年龄，确保合理间隔
        keyNodeAges.put("start", 0);  // 出生
        keyNodeAges.put("infant_2", 4);  // 幼儿期
        keyNodeAges.put("childhood_1", 7);  // 小学入学
        keyNodeAges.put("childhood_2", 10);  // 小学中年级
        keyNodeAges.put("childhood_3", 13); // 小学毕业
        keyNodeAges.put("teenager_1", 14);  // 初中入学
        keyNodeAges.put("teenager_2", 16);  // 初中毕业
        keyNodeAges.put("teenager_3", 17);  // 高中
        keyNodeAges.put("high_school_graduation", 19); // 高中毕业
        keyNodeAges.put("college_1", 20);   // 大学一年级
        keyNodeAges.put("college_2", 22);   // 大学三年级
        keyNodeAges.put("college_graduation", 24); // 大学毕业
        keyNodeAges.put("career_start", 26); // 职业开始
        keyNodeAges.put("career_1", 30);    // 职业发展
        keyNodeAges.put("career_2", 38);    // 职业中期
        keyNodeAges.put("midlife_crisis", 46); // 中年
        keyNodeAges.put("midlife_2", 56);   // 中年后期
        keyNodeAges.put("retirement_decision", 62); // 退休决策
        keyNodeAges.put("elder_1", 72);     // 老年初期
        keyNodeAges.put("game_end", 82);    // 游戏结束
    }

    private void buildRandomEvents() {
        // 重新设计随机事件，确保年龄阶段合理

        // 婴幼儿期随机事件 (1-5岁)
        randomEvents.add(new RandomEvent("infant_random_1",
                "你学会了爬行，探索家里的每个角落",
                Arrays.asList("继续探索", "休息一下"),
                new AttributeEffect(0, 3, 1, 2)));

        randomEvents.add(new RandomEvent("infant_random_2",
                "你第一次开口说话，父母非常高兴",
                Arrays.asList("多说几句话", "害羞地躲起来"),
                new AttributeEffect(0, 1, 4, 2)));

        randomEvents.add(new RandomEvent("infant_random_3",
                "你开始学习走路，经常摔倒",
                Arrays.asList("勇敢站起来", "寻求帮助"),
                new AttributeEffect(0, 1, 3, 1)));

        randomEvents.add(new RandomEvent("infant_random_4",
                "你对周围的一切都充满好奇",
                Arrays.asList("积极探索", "观察学习"),
                new AttributeEffect(0, 1, 3, 1)));

        randomEvents.add(new RandomEvent("infant_random_5",
                "你开始上幼儿园",
                Arrays.asList("积极参与活动", "适应新环境"),
                new AttributeEffect(0, 1, 3, 2)));

        // 童年期随机事件 (6-12岁)
        randomEvents.add(new RandomEvent("childhood_random_1",
                "你在学校交到了新朋友",
                Arrays.asList("一起玩耍", "邀请来家里做客"),
                new AttributeEffect(0, 1, 5, 1)));

        randomEvents.add(new RandomEvent("childhood_random_2",
                "你在运动会上表现出色",
                Arrays.asList("继续训练", "尝试其他运动"),
                new AttributeEffect(0, 4, 2, 0)));

        randomEvents.add(new RandomEvent("childhood_random_3",
                "你在考试中取得了好成绩",
                Arrays.asList("继续努力", "放松一下"),
                new AttributeEffect(0, 0, 4, 5)));

        randomEvents.add(new RandomEvent("childhood_random_4",
                "你参加了学校的艺术节",
                Arrays.asList("表演节目", "观看演出"),
                new AttributeEffect(0, 1, 3, 1)));

        randomEvents.add(new RandomEvent("childhood_random_5",
                "你迷上了阅读",
                Arrays.asList("每天读书", "适度阅读"),
                new AttributeEffect(0, -1, 2, 3)));

        randomEvents.add(new RandomEvent("childhood_random_6",
                "家庭旅行计划",
                Arrays.asList("积极参与", "选择在家"),
                new AttributeEffect(-2, 2, 4, 1)));

        // 青少年期随机事件 (13-17岁)
        randomEvents.add(new RandomEvent("teenager_random_1",
                "你参加了学校的社团活动",
                Arrays.asList("积极参与", "观望一下"),
                new AttributeEffect(0, 1, 4, 2)));

        randomEvents.add(new RandomEvent("teenager_random_2",
                "你遇到了青春期的烦恼",
                Arrays.asList("向父母倾诉", "自己解决"),
                new AttributeEffect(0, -3, -5, 2)));

        randomEvents.add(new RandomEvent("teenager_random_3",
                "你在学科竞赛中获奖",
                Arrays.asList("继续努力", "尝试新领域"),
                new AttributeEffect(0, 2, 6, 7)));

        randomEvents.add(new RandomEvent("teenager_random_4",
                "面临文理分科选择",
                Arrays.asList("选择文科", "选择理科"),
                new AttributeEffect(0, 0, 2, 2)));

        randomEvents.add(new RandomEvent("teenager_random_5",
                "参加志愿者活动",
                Arrays.asList("积极参与", "偶尔参加"),
                new AttributeEffect(0, 1, 3, 2)));

        randomEvents.add(new RandomEvent("teenager_random_6",
                "学习压力增大",
                Arrays.asList("寻求帮助", "自己调整"),
                new AttributeEffect(0, -2, -3, 2)));

        // 在 DecisionTreeGameService.java 的 buildRandomEvents() 方法中，替换大学及以后阶段的事件

// 大学期随机事件 (18-24岁)
        randomEvents.add(new RandomEvent("college_random_1",
                "你在大学里选择了专业方向",
                Arrays.asList("专注学术研究", "参与社会实践"),
                new AttributeEffect(0, 0, 2, 5)));

        randomEvents.add(new RandomEvent("college_random_2",
                "你参加了实习工作",
                Arrays.asList("努力工作积累经验", "探索其他可能性"),
                new AttributeEffect(5, -2, 1, 4)));

        randomEvents.add(new RandomEvent("college_random_3",
                "你遇到了心仪的对象",
                Arrays.asList("主动追求", "顺其自然"),
                new AttributeEffect(-2, 0, 6, 0)));

        randomEvents.add(new RandomEvent("college_random_4",
                "大学生活丰富多彩，你选择参加",
                Arrays.asList("学术竞赛", "社团活动", "志愿者服务"),
                new AttributeEffect(0, 2, 4, 3)));

        randomEvents.add(new RandomEvent("college_random_5",
                "面临期末考试压力",
                Arrays.asList("熬夜复习", "合理安排时间", "寻求帮助"),
                new AttributeEffect(0, -2, -2, 3)));

        randomEvents.add(new RandomEvent("college_random_6",
                "获得奖学金机会",
                Arrays.asList("继续努力", "适度放松"),
                new AttributeEffect(4, 0, 2, 2)));

        randomEvents.add(new RandomEvent("college_random_7",
                "参加校园创业大赛",
                Arrays.asList("积极参与", "观摩学习"),
                new AttributeEffect(2, -1, 1, 3)));

        randomEvents.add(new RandomEvent("college_random_8",
                "面临留学选择",
                Arrays.asList("申请留学", "国内发展"),
                new AttributeEffect(-5, 0, -2, 6)));

        randomEvents.add(new RandomEvent("college_random_9",
                "与室友相处",
                Arrays.asList("主动沟通", "保持距离"),
                new AttributeEffect(0, 0, 4, 1)));

        randomEvents.add(new RandomEvent("college_random_10",
                "参加校园招聘会",
                Arrays.asList("积极投递简历", "观望等待"),
                new AttributeEffect(2, 0, 1, 0)));

// 青年期随机事件 (25-44岁)
        randomEvents.add(new RandomEvent("youth_random_1",
                "你在工作中遇到挑战",
                Arrays.asList("积极应对", "寻求帮助"),
                new AttributeEffect(0, -2, -2, 3)));

        randomEvents.add(new RandomEvent("youth_random_2",
                "你获得了晋升机会",
                Arrays.asList("接受挑战", "保持现状"),
                new AttributeEffect(5, -3, 3, 2)));

        randomEvents.add(new RandomEvent("youth_random_3",
                "家庭生活带来幸福感",
                Arrays.asList("多陪伴家人", "专注事业发展"),
                new AttributeEffect(0, 2, 5, 0)));

        randomEvents.add(new RandomEvent("youth_random_4",
                "购买第一套房子",
                Arrays.asList("贷款购房", "继续租房"),
                new AttributeEffect(-10, 0, 5, 0)));

        randomEvents.add(new RandomEvent("youth_random_5",
                "面临职业转型",
                Arrays.asList("勇敢尝试", "保持稳定"),
                new AttributeEffect(-2, -2, -1, 3)));

        randomEvents.add(new RandomEvent("youth_random_6",
                "孩子出生",
                Arrays.asList("全心照顾", "平衡工作家庭"),
                new AttributeEffect(-4, -2, 5, 0)));

        randomEvents.add(new RandomEvent("youth_random_7",
                "投资理财机会",
                Arrays.asList("谨慎投资", "保守理财"),
                new AttributeEffect(5, 0, 2, 2)));

        randomEvents.add(new RandomEvent("youth_random_8",
                "参加职业培训",
                Arrays.asList("积极学习", "应付了事"),
                new AttributeEffect(-2, 0, 1, 4)));

        randomEvents.add(new RandomEvent("youth_random_9",
                "工作压力增大",
                Arrays.asList("调整心态", "寻求支持"),
                new AttributeEffect(0, -4, -2, 1)));

        randomEvents.add(new RandomEvent("youth_random_10",
                "获得重要项目",
                Arrays.asList("全力以赴", "寻求合作"),
                new AttributeEffect(6, -2, 1, 4)));

        randomEvents.add(new RandomEvent("youth_random_11",
                "家庭旅行计划",
                Arrays.asList("精心策划", "简单出行"),
                new AttributeEffect(-2, 3, 4, 1)));

        randomEvents.add(new RandomEvent("youth_random_12",
                "面临跳槽机会",
                Arrays.asList("接受挑战", "保持稳定"),
                new AttributeEffect(4, -1, 0, 3)));

// 中年期随机事件 (45-64岁)
        randomEvents.add(new RandomEvent("middle_random_1",
                "你的工作压力增大",
                Arrays.asList("加班应对", "调整工作方式"),
                new AttributeEffect(5, -5, -4, 2)));

        randomEvents.add(new RandomEvent("middle_random_2",
                "健康检查发现问题",
                Arrays.asList("积极治疗", "忽视问题"),
                new AttributeEffect(-10, -10, -5, 0)));

        randomEvents.add(new RandomEvent("middle_random_3",
                "孩子教育成为关注重点",
                Arrays.asList("投入更多精力", "保持现状"),
                new AttributeEffect(-5, 0, 1, 1)));

        randomEvents.add(new RandomEvent("middle_random_4",
                "面临职业瓶颈",
                Arrays.asList("突破自我", "安于现状"),
                new AttributeEffect(0, -3, -2, 3)));

        randomEvents.add(new RandomEvent("middle_random_5",
                "父母健康需要关注",
                Arrays.asList("悉心照料", "请人照顾"),
                new AttributeEffect(-4, 0, -2, 0)));

        randomEvents.add(new RandomEvent("middle_random_6",
                "获得行业荣誉",
                Arrays.asList("继续努力", "享受成就"),
                new AttributeEffect(5, 0, 5, 2)));

        randomEvents.add(new RandomEvent("middle_random_7",
                "投资副业",
                Arrays.asList("大胆尝试", "谨慎观望"),
                new AttributeEffect(10, 0, 3, 3)));

        randomEvents.add(new RandomEvent("middle_random_8",
                "身体出现亚健康",
                Arrays.asList("积极锻炼", "忽视问题"),
                new AttributeEffect(0, 8, 5, 0)));

        randomEvents.add(new RandomEvent("middle_random_9",
                "孩子上大学",
                Arrays.asList("全力支持", "适度帮助"),
                new AttributeEffect(-6, 0, 3, 0)));

        randomEvents.add(new RandomEvent("middle_random_10",
                "工作与生活平衡",
                Arrays.asList("调整节奏", "维持现状"),
                new AttributeEffect(0, 2, 4, 0)));

        randomEvents.add(new RandomEvent("middle_random_11",
                "面临裁员风险",
                Arrays.asList("积极应对", "寻找新机会"),
                new AttributeEffect(-5, -2, -4, 0)));

        randomEvents.add(new RandomEvent("middle_random_12",
                "参与社区活动",
                Arrays.asList("积极投入", "偶尔参加"),
                new AttributeEffect(0, 3, 4, 0)));

// 老年期随机事件 (65岁及以上)
        randomEvents.add(new RandomEvent("elder_random_1",
                "你开始享受退休生活",
                Arrays.asList("发展新爱好", "安享晚年"),
                new AttributeEffect(0, 2, 5, 1)));

        randomEvents.add(new RandomEvent("elder_random_2",
                "健康状况需要关注",
                Arrays.asList("定期检查", "顺其自然"),
                new AttributeEffect(-5, -8, -3, 0)));

        randomEvents.add(new RandomEvent("elder_random_3",
                "与家人共度美好时光",
                Arrays.asList("多组织家庭聚会", "享受宁静"),
                new AttributeEffect(0, 3, 5, 0)));

        randomEvents.add(new RandomEvent("elder_random_4",
                "孙辈出生",
                Arrays.asList("帮忙照顾", "偶尔探望"),
                new AttributeEffect(0, -2, 3, 0)));

        randomEvents.add(new RandomEvent("elder_random_5",
                "参加老年大学",
                Arrays.asList("积极学习", "偶尔参加"),
                new AttributeEffect(-2, 3, 4, 2)));

        randomEvents.add(new RandomEvent("elder_random_6",
                "健康状况改善",
                Arrays.asList("坚持锻炼", "适度休息"),
                new AttributeEffect(0, 5, 2, 0)));

        randomEvents.add(new RandomEvent("elder_random_7",
                "与老友相聚",
                Arrays.asList("经常联系", "偶尔见面"),
                new AttributeEffect(0, 2, 3, 0)));

        randomEvents.add(new RandomEvent("elder_random_8",
                "整理人生回忆",
                Arrays.asList("认真记录", "顺其自然"),
                new AttributeEffect(0, 0, 4, 2)));

        randomEvents.add(new RandomEvent("elder_random_9",
                "面临健康挑战",
                Arrays.asList("积极治疗", "保守治疗"),
                new AttributeEffect(-8, -10, -3, 0)));

        randomEvents.add(new RandomEvent("elder_random_10",
                "享受天伦之乐",
                Arrays.asList("多与家人相处", "保持独立空间"),
                new AttributeEffect(0, 2, 6, 0)));

        randomEvents.add(new RandomEvent("elder_random_11",
                "参与志愿活动",
                Arrays.asList("积极参与", "偶尔参加"),
                new AttributeEffect(0, 3, 3, 0)));

        randomEvents.add(new RandomEvent("elder_random_12",
                "回顾人生经历",
                Arrays.asList("总结经验", "随缘看待"),
                new AttributeEffect(0, 0, 3, 0)));
    }

    private void buildDecisionTree() {

        // === 婴幼儿期关键节点 ===
        decisionTree.put("start", new DecisionNode(
                "start", 0,
                "你出生在一个普通的家庭，父母对你的期望是...",
                Arrays.asList("健康快乐成长", "将来有所成就", "人生顺利美满"),
                Arrays.asList("infant_2", "infant_2", "infant_2"),
                Arrays.asList(
                        new AttributeEffect(0, 3, 2, 0),  // 健康快乐成长：侧重健康和快乐
                        new AttributeEffect(0, 1, 1, 3),   // 将来有所成就：侧重知识和健康
                        new AttributeEffect(0, 3, 2, 1)     // 人生顺利美满：均衡发展
                )
        ));

        decisionTree.put("infant_2", new DecisionNode(
                "infant_2", 4,
                "作为幼儿，你的早期发展重点是...",
                Arrays.asList("身体运动能力", "语言沟通能力", "社交互动能力"),
                Arrays.asList("childhood_1", "childhood_1", "childhood_1"),
                Arrays.asList(
                        new AttributeEffect(0, 4, 2, 0),   // 身体运动能力：侧重健康
                        new AttributeEffect(0, 0, 2, 3),    // 语言沟通能力：侧重知识和快乐
                        new AttributeEffect(0, 1, 2, 2)    // 社交互动能力：侧重快乐
                )
        ));

        // === 童年期关键节点 ===
        decisionTree.put("childhood_1", new DecisionNode(
                "childhood_1", 7,
                "进入小学，你的学习态度是...",
                Arrays.asList("认真学习，争取好成绩", "培养兴趣爱好", "快乐玩耍，享受童年"),
                Arrays.asList("childhood_2", "childhood_2", "childhood_2"),
                Arrays.asList(
                        new AttributeEffect(0, -2, -1, 5),   // 认真学习：侧重知识
                        new AttributeEffect(0, 0, 4, 3),    // 培养兴趣：均衡快乐和知识
                        new AttributeEffect(0, 3, 3, -2)    // 快乐学习：侧重快乐
                )
        ));

        decisionTree.put("childhood_2", new DecisionNode(
                "childhood_2", 10,
                "小学中年级，你面临的选择是...",
                Arrays.asList("参加课外辅导班", "加入兴趣小组", "更多时间玩耍"),
                Arrays.asList("childhood_3", "childhood_3", "childhood_3"),
                Arrays.asList(
                        new AttributeEffect(0, -2, -3, 3), // 课外辅导：知识增加但健康快乐减少
                        new AttributeEffect(0, 1, 3, 1),    // 兴趣小组：均衡发展
                        new AttributeEffect(0, 3, 5, -2)   // 更多玩耍：快乐增加但知识减少
                )
        ));

        decisionTree.put("childhood_3", new DecisionNode(
                "childhood_3", 13,
                "小学毕业，即将进入中学，你准备...",
                Arrays.asList("提前预习中学课程", "好好享受假期", "发展个人特长"),
                Arrays.asList("teenager_1", "teenager_1", "teenager_1"),
                Arrays.asList(
                        new AttributeEffect(0, -3, -2, 5), // 提前预习：知识增加但健康快乐减少
                        new AttributeEffect(0, 2, 4, -1),  // 享受假期：快乐大幅增加
                        new AttributeEffect(0, 2, 5, 3)     // 发展特长：均衡发展
                )
        ));

        // === 青少年期关键节点 ===
        decisionTree.put("teenager_1", new DecisionNode(
                "teenager_1", 14,
                "进入初中，面临新的学习环境，你选择...",
                Arrays.asList("专注学习，争取好成绩", "参加社团活动", "结交新朋友"),
                Arrays.asList("teenager_2", "teenager_2", "teenager_2"),
                Arrays.asList(
                        new AttributeEffect(0, -4, -3, 5), // 专注学习：知识大幅增加
                        new AttributeEffect(0, 1, 3, 3),    // 社团活动：快乐和知识均衡
                        new AttributeEffect(0, 1, 4, -2)    // 结交朋友：快乐大幅增加
                )
        ));

        decisionTree.put("teenager_2", new DecisionNode(
                "teenager_2", 16,
                "初中毕业，面临升学选择...",
                Arrays.asList("努力考取重点高中", "选择普通高中", "考虑职业学校"),
                Arrays.asList("teenager_3", "teenager_3", "teenager_3"),
                Arrays.asList(
                        new AttributeEffect(0, -5, -1, 6), // 重点高中：知识大幅增加
                        new AttributeEffect(0, -2, 2, 4),  // 普通高中：知识适度增加
                        new AttributeEffect(5, 2, 2, -1)     // 职业学校：财富和快乐增加
                )
        ));

        decisionTree.put("teenager_3", new DecisionNode(
                "teenager_3", 17,
                "高中阶段，学习压力增大，你如何应对？",
                Arrays.asList("刻苦学习备战高考", "平衡学习与兴趣", "注重全面发展"),
                Arrays.asList("high_school_graduation", "high_school_graduation", "high_school_graduation"),
                Arrays.asList(
                        new AttributeEffect(0, -2, -2, 5), // 刻苦学习：知识大幅增加但健康快乐减少
                        new AttributeEffect(0, -1, 2, 3),  // 平衡发展：适度增加知识
                        new AttributeEffect(0, 2, 3, 1)    // 全面发展：均衡发展
                )
        ));

        // === 高中毕业关键节点 ===
        decisionTree.put("high_school_graduation", new DecisionNode(
                "high_school_graduation", 19,
                "高中毕业，面临人生重要选择...",
                Arrays.asList("参加高考上大学", "学习技术就业", "直接工作积累经验"),
                nextNodeByKnowledge("college_1", "career_start", "career_start"),
                Arrays.asList(
                        new AttributeEffect(0, 0, 2, 3),  // 上大学：知识大幅增加
                        new AttributeEffect(5, -2, 3, 3),   // 学技术：财富和知识增加
                        new AttributeEffect(7, -2, 2, 3)    // 直接工作：财富大幅增加
                )
        ));

        // === 大学毕业关键节点 ===
        decisionTree.put("college_1", new DecisionNode(
                "college_1", 20,
                "大学生活开始，你如何规划你的大学生活？",
                Arrays.asList("专注学术研究", "参与社团活动", "发展个人兴趣"),
                Arrays.asList("college_2", "college_2", "college_2"),
                Arrays.asList(
                        new AttributeEffect(0, -3, -2, 5), // 学术研究：知识大幅增加
                        new AttributeEffect(0, 2, 3, 2),   // 社团活动：快乐大幅增加
                        new AttributeEffect(0, 2, 3, 4)    // 个人兴趣：快乐增加
                )
        ));

        decisionTree.put("college_2", new DecisionNode(
                "college_2", 22,
                "大学生活过半，你面临的选择是...",
                Arrays.asList("准备考研深造", "寻找实习机会", "享受大学生活"),
                Arrays.asList("college_graduation", "college_graduation", "college_graduation"),
                Arrays.asList(
                        new AttributeEffect(0, -2, -3, 5), // 考研深造：知识增加
                        new AttributeEffect(5, -1, 2, 3),  // 寻找实习：财富和知识增加
                        new AttributeEffect(0, 3, 5, 0)    // 享受生活：快乐大幅增加
                )
        ));

        decisionTree.put("college_graduation", new DecisionNode(
                "college_graduation", 24,
                "大学毕业，就业选择...",
                Arrays.asList("大公司稳定工作", "创业追求梦想", "继续深造读研"),
                Arrays.asList("career_start", "career_start", "career_start"),
                Arrays.asList(
                        new AttributeEffect(7, -1, 3, 0),  // 大公司：财富增加
                        new AttributeEffect(5, -2, 2, 0),   // 创业：风险和回报并存
                        new AttributeEffect(-3, -2, -3, 5)   // 读研：知识大幅增加
                )
        ));

        // === 职业生涯开始关键节点 ===
        decisionTree.put("career_start", new DecisionNode(
                "career_start", 26,
                "职业生涯正式开始，你注重...",
                Arrays.asList("努力工作晋升", "工作生活平衡", "积累人脉资源"),
                Arrays.asList("career_1", "career_1", "career_1"),
                Arrays.asList(
                        new AttributeEffect(5, -1, -2, 0), // 努力工作：财富增加但健康快乐减少
                        new AttributeEffect(2, 2, 2, 0),  // 工作平衡：均衡发展
                        new AttributeEffect(3, -1, 3, 0)    // 积累人脉：财富快乐知识均衡
                )
        ));

        decisionTree.put("career_1", new DecisionNode(
                "career_1", 30,
                "工作几年后，你面临职业发展的选择...",
                Arrays.asList("追求晋升机会", "寻求工作生活平衡", "考虑职业转型"),
                Arrays.asList("midlife_crisis", "midlife_crisis", "midlife_crisis"),
                Arrays.asList(
                        new AttributeEffect(10, -3, -2, 0), // 追求晋升：财富增加
                        new AttributeEffect(4, 4, 6, 0),   // 工作平衡：快乐健康增加
                        new AttributeEffect(-5, -3, 3, 0)   // 职业转型：知识增加
                )
        ));

        // === 中年危机关键节点 ===
        decisionTree.put("midlife_crisis", new DecisionNode(
                "midlife_crisis", 46,
                "步入中年，面临生活压力，你选择...",
                Arrays.asList("专注事业发展", "重视家庭生活", "寻求生活改变"),
                nextNodeByHappiness("midlife_2", "midlife_2", "midlife_2"),
                Arrays.asList(
                        new AttributeEffect(10, -4, 2, 0), // 专注事业：财富增加
                        new AttributeEffect(0, 5, 5, 0),   // 重视家庭：快乐健康增加
                        new AttributeEffect(-5, 3, 4, 6)     // 寻求改变：均衡调整
                )
        ));

        decisionTree.put("midlife_2", new DecisionNode(
                "midlife_2", 56,
                "中年后期，你思考更多的是...",
                Arrays.asList("规划退休生活", "继续事业发展", "关注健康养生"),
                Arrays.asList("retirement_decision", "retirement_decision", "retirement_decision"),
                Arrays.asList(
                        new AttributeEffect(0, 4, 4, 0),   // 规划退休：快乐健康增加
                        new AttributeEffect(8, -5, -2, 0),  // 继续事业：财富增加
                        new AttributeEffect(-5, 6, 4, 0)    // 关注健康：健康大幅增加
                )
        ));

        // === 退休决策关键节点 ===
        decisionTree.put("retirement_decision", new DecisionNode(
                "retirement_decision", 62,
                "到了退休年龄，你决定...",
                Arrays.asList("正式退休安享晚年", "继续工作发挥余热", "半退休享受生活"),
                Arrays.asList("elder_1", "elder_1", "elder_1"),
                Arrays.asList(
                        new AttributeEffect(0, -4, 3, 0),   // 正式退休：快乐健康增加
                        new AttributeEffect(10, -7, -5, 0),  // 继续工作：财富增加
                        new AttributeEffect(-5, -6, 2, 0)    // 半退休：均衡发展
                )
        ));

        decisionTree.put("elder_1", new DecisionNode(
                "elder_1", 72,
                "进入老年生活，你的态度是...",
                Arrays.asList("积极面对生活", "享受宁静时光", "继续学习成长"),
                Arrays.asList("game_end", "game_end", "game_end"),
                Arrays.asList(
                        new AttributeEffect(0, 4, 4, 0),   // 积极面对：全面增益
                        new AttributeEffect(0, 2, 4, 0),   // 享受宁静：快乐大幅增加
                        new AttributeEffect(0, -5, 3, 10)    // 继续学习：知识增加
                )
        ));

        // === 游戏结束 ===
        decisionTree.put("game_end", new DecisionNode(
                "game_end", 82,
                "人生旅程结束",
                Arrays.asList("查看人生总结", "开始新游戏"),
                Arrays.asList("life_summary", "new_game"),
                Arrays.asList(
                        new AttributeEffect(0, 0, 0, 0),
                        new AttributeEffect(0, 0, 0, 0)
                )
        ));
    }

    // 更新 getRandomEvent 方法中的年龄阶段判断
    private RandomEvent getRandomEvent(String currentKeyNode, String nextKeyNode, GameState state) {
        List<RandomEvent> availableEvents = new ArrayList<>();

        int currentAge = keyNodeAges.get(currentKeyNode);
        int nextAge = keyNodeAges.get(nextKeyNode);

        for (RandomEvent event : randomEvents) {
            String eventId = event.getId();

            // 根据事件ID前缀和当前年龄阶段筛选合适的事件
            if (eventId.contains("infant") && currentAge >= 0 && currentAge < 6) {
                availableEvents.add(event);
            } else if (eventId.contains("childhood") && currentAge >= 6 && currentAge < 13) {
                availableEvents.add(event);
            } else if (eventId.contains("teenager") && currentAge >= 13 && currentAge < 19) {
                availableEvents.add(event);
            } else if (eventId.contains("college") && currentAge >= 19 && currentAge < 23) {
                availableEvents.add(event);
            } else if (eventId.contains("youth") && currentAge >= 23 && currentAge < 45) {
                availableEvents.add(event);
            } else if (eventId.contains("middle") && currentAge >= 45 && currentAge < 65) {
                availableEvents.add(event);
            } else if (eventId.contains("elder") && currentAge >= 65) {
                availableEvents.add(event);
            }
        }

        if (availableEvents.isEmpty()) {
            return null;
        }

        return availableEvents.get(random.nextInt(availableEvents.size()));
    }

    // 修改获取随机年龄的方法，确保唯一性
    private int getRandomAgeBetween(String currentKeyNode, String nextKeyNode, GameState state) {
        int minAge = keyNodeAges.get(currentKeyNode);
        int maxAge = keyNodeAges.get(nextKeyNode);

        // 获取已使用的年龄
        Set<Integer> usedAges = getUsedAgesFromEvents(state);
        usedAges.add(minAge); // 当前关键节点年龄已使用
        usedAges.add(maxAge); // 下一个关键节点年龄将被使用

        // 在范围内找到未使用的年龄
        List<Integer> availableAges = new ArrayList<>();
        for (int age = minAge + 1; age < maxAge; age++) {
            if (!usedAges.contains(age)) {
                availableAges.add(age);
            }
        }

        if (availableAges.isEmpty()) {
            // 如果没有可用年龄，返回中间值
            return minAge + 1;
        }

        return availableAges.get(random.nextInt(availableAges.size()));
    }

    // 从事件历史中提取已使用的年龄
    private Set<Integer> getUsedAgesFromEvents(GameState state) {
        Set<Integer> usedAges = new HashSet<>();
        for (String event : state.getLifeEvents()) {
            if (event.startsWith("年龄") && event.contains("岁:")) {
                try {
                    String ageStr = event.substring(2, event.indexOf("岁:"));
                    usedAges.add(Integer.parseInt(ageStr));
                } catch (Exception e) {
                    // 解析失败，忽略
                }
            }
        }
        return usedAges;
    }

    // 在 processDecision 方法开始处添加更严格的检查
    public GameState processDecision(GameState state, int choiceIndex) {
        if (state == null) {
            return initializeGame();
        }

        if (state.isGameEnded()) {
            return handleGameEnd(state, choiceIndex);
        }

        // 检查是否是随机事件
        if ("random_event".equals(state.getCurrentNodeId())) {
            return handleRandomEventChoice(state, choiceIndex);
        }

        String currentNodeId = state.getCurrentNodeId();
        if (currentNodeId == null) {
            currentNodeId = "start";
            state.setCurrentNodeId(currentNodeId);
        }

        DecisionNode currentNode = decisionTree.get(currentNodeId);
        if (currentNode == null) {
            // 如果节点不存在，回到起点
            currentNode = decisionTree.get("start");
            state.setCurrentNodeId("start");
        }

        // 确保 choiceIndex 在有效范围内
        if (choiceIndex < 0 || choiceIndex >= currentNode.getNextNodeIds().size()) {
            choiceIndex = 0; // 默认选择第一个选项
        }

        String nextNodeId = currentNode.getNextNodeIds().get(choiceIndex);

        // 确保下一个节点存在
        if (!decisionTree.containsKey(nextNodeId)) {
            nextNodeId = "game_end"; // 如果节点不存在，跳转到结束
        }

        // 检查是否是关键节点之间的过渡 - 修改触发条件
        if (isKeyNode(currentNodeId) && isKeyNode(nextNodeId) &&
                !currentNodeId.equals(nextNodeId) &&
                keyNodeAges.get(nextNodeId) - keyNodeAges.get(currentNodeId) > 2) { // 只有年龄差距大于2才触发随机事件

            // 在关键节点之间插入随机事件
            RandomEvent randomEvent = getRandomEvent(currentNodeId, nextNodeId, state);
            if (randomEvent != null && random.nextDouble() < 0.6) { // 60%概率触发随机事件
                return handleRandomEvent(state, randomEvent, nextNodeId, currentNode, choiceIndex);
            }
        }

        return moveToNextNode(state, currentNode, nextNodeId, choiceIndex);
    }

    private boolean isKeyNode(String nodeId) {
        return keyNodeAges.containsKey(nodeId);
    }

    // 修改 handleRandomEvent 方法，传递更多参数以便记录关键节点选择
    private GameState handleRandomEvent(GameState state, RandomEvent randomEvent, String nextKeyNodeId,
                                      DecisionNode currentNode, int choiceIndex) {
        // 首先记录关键节点的选择
        if (isKeyNode(currentNode.getId()) && !currentNode.getId().equals("start")) {
            String currentChoice = currentNode.getChoices().get(choiceIndex);
            state.addEvent(currentNode.getDescription() + " → " + currentChoice);

            // 应用关键节点的属性效果
            List<AttributeEffect> effects = currentNode.getEffects();
            if (effects != null && choiceIndex >= 0 && choiceIndex < effects.size()) {
                AttributeEffect effect = effects.get(choiceIndex);
                if (effect != null) {
                    state.applyEffect(effect.getWealthChange(), effect.getHealthChange(),
                            effect.getHappinessChange(), effect.getKnowledgeChange());
                }
            }
        }

        // 设置随机事件信息
        state.setCurrentNodeId("random_event");
        state.setCurrentEvent(randomEvent.getDescription());
        state.setCurrentChoices(randomEvent.getChoices());
        state.setPendingKeyNode(nextKeyNodeId);
        state.setPendingRandomEvent(randomEvent);

        // 更新年龄到随机事件年龄
        int randomAge = getRandomAgeBetween(state.getLastKeyNode(), nextKeyNodeId, state);
        state.setAge(randomAge);

        return state;
    }

    private GameState handleRandomEventChoice(GameState state, int choiceIndex) {
        RandomEvent randomEvent = state.getPendingRandomEvent();
        String nextKeyNodeId = state.getPendingKeyNode();

        if (randomEvent != null && choiceIndex >= 0 && choiceIndex < randomEvent.getChoices().size()) {
            // 应用随机事件效果
            AttributeEffect effect = randomEvent.getEffect();
            if (effect != null) {
                state.applyEffect(effect.getWealthChange(), effect.getHealthChange(),
                        effect.getHappinessChange(), effect.getKnowledgeChange());
            }

            // 记录随机事件选择 - 使用当前年龄
            state.addEvent(randomEvent.getDescription() + " → " + randomEvent.getChoices().get(choiceIndex));

            // 检查游戏结束条件
            if (checkGameEndCondition(state)) {
                return state;
            }

            // 移动到下一个关键节点，但不再记录关键节点选择（已在handleRandomEvent中记录）
            return moveToKeyNode(state, nextKeyNodeId);
        }

        return state;
    }

    // 新增方法：直接移动到关键节点，不记录选择
    private GameState moveToKeyNode(GameState state, String nextNodeId) {
        DecisionNode nextNode = decisionTree.get(nextNodeId);

        // 更新年龄到关键节点年龄
        if (isKeyNode(nextNodeId)) {
            state.setAge(keyNodeAges.get(nextNodeId));
            state.setLastKeyNode(nextNodeId);
        }

        // 移动到下一个节点
        state.setCurrentNodeId(nextNodeId);
        state.setCurrentEvent(nextNode.getDescription());
        state.setCurrentChoices(nextNode.getChoices());
        state.setPendingKeyNode(null);
        state.setPendingRandomEvent(null);

        // 检查游戏结束条件
        checkGameEndCondition(state);

        return state;
    }

    // 修改 moveToNextNode 方法
    private GameState moveToNextNode(GameState state, DecisionNode currentNode, String nextNodeId, int choiceIndex) {
        DecisionNode nextNode = decisionTree.get(nextNodeId);

        // 应用属性效果 - 根据选择索引获取对应的效果
        List<AttributeEffect> effects = currentNode.getEffects();
        if (effects != null && choiceIndex >= 0 && choiceIndex < effects.size()) {
            AttributeEffect effect = effects.get(choiceIndex);
            if (effect != null) {
                state.applyEffect(effect.getWealthChange(), effect.getHealthChange(),
                        effect.getHappinessChange(), effect.getKnowledgeChange());
            }
        }

        // 记录关键节点选择 - 在更新年龄之前记录
        if (isKeyNode(currentNode.getId()) && !currentNode.getId().equals("start")) {
            state.addEvent(currentNode.getDescription() + " → " + currentNode.getChoices().get(choiceIndex));
        }

        // 更新年龄
        if (isKeyNode(nextNodeId)) {
            state.setAge(keyNodeAges.get(nextNodeId));
            state.setLastKeyNode(nextNodeId);
        }

        // 移动到下一个节点
        state.setCurrentNodeId(nextNodeId);
        state.setCurrentEvent(nextNode.getDescription());
        state.setCurrentChoices(nextNode.getChoices());
        state.setPendingKeyNode(null);
        state.setPendingRandomEvent(null);

        // 检查游戏结束条件
        checkGameEndCondition(state);

        return state;
    }

    private boolean checkGameEndCondition(GameState state) {
        // 检查属性边界
        if (state.getHealth() <= 0 || state.getHealth() >= 100 ||
                state.getWealth() <= 0 || state.getWealth() >= 100 ||
                state.getHappiness() <= 0 || state.getHappiness() >= 100 ||
                state.getKnowledge() <= 0 || state.getKnowledge() >= 100) {

            state.setGameEnded(true);
            state.setCurrentEvent("人生旅程结束\n\n" + generateEnding(state));
            state.setCurrentChoices(Arrays.asList("查看人生总结", "开始新游戏"));
            return true;
        }

        // 检查到达最终节点
        if (state.getCurrentNodeId().equals("game_end")) {
            state.setGameEnded(true);
            state.setCurrentEvent(state.getCurrentEvent() + "\n\n" + generateEnding(state));
            state.setCurrentChoices(Arrays.asList("查看人生总结", "开始新游戏"));
            return true;
        }

        return false;
    }

    private GameState handleGameEnd(GameState state, int choiceIndex) {
        if (choiceIndex == 0) {
            // 查看人生总结
            state.setCurrentEvent(generateLifeSummary(state));
            state.setCurrentChoices(Arrays.asList("开始新游戏"));
        } else {
            // 开始新游戏 - 创建新状态
            return initializeGame();
        }
        return state;
    }

    private String generateEnding(GameState state) {
        int age = state.getAge();

        // 健康归零的结局
        if (state.getHealth() <= 0) {
            if (age < 18) return "不幸早夭，生命如此短暂";
            else if (age < 40) return "英年早逝，令人惋惜";
            else if (age < 60) return "中年离世，未尽的事业成为遗憾";
            else return "寿终正寝，安详离世";
        }

        // 财富满额的结局
        if (state.getWealth() >= 100) {
            if (age < 40) return "年轻富豪，财富自由的人生";
            else return "富甲一方，享受奢华晚年";
        }

        // 知识满额的结局
        if (state.getKnowledge() >= 100) {
            return "学富五车，成为受人尊敬的学者";
        }

        // 幸福满额的结局
        if (state.getHappiness() >= 100) {
            return "幸福圆满，快乐的一生";
        }

        // 健康满额的结局
        if (state.getHealth() >= 100) {
            return "健康长寿，无疾而终";
        }

        // 根据年龄和属性综合判断
        if (age >= 80) {
            if (state.getHappiness() >= 70) return "颐养天年，安享晚年";
            else return "平淡人生，安然离世";
        } else if (age >= 65) {
            return "退休生活，享受闲暇时光";
        } else if (age >= 45) {
            return "中年稳定，事业家庭平衡";
        } else {
            return "人生旅程还在继续...";
        }
    }

    private String generateLifeSummary(GameState state) {
        StringBuilder summary = new StringBuilder();
        summary.append("=== 人生总结 ===\n");
        summary.append("最终年龄: ").append(state.getAge()).append("岁\n");
        summary.append("财富: ").append(state.getWealth()).append("\n");
        summary.append("健康: ").append(state.getHealth()).append("\n");
        summary.append("幸福: ").append(state.getHappiness()).append("\n");
        summary.append("知识: ").append(state.getKnowledge()).append("\n\n");
        summary.append("人生经历:\n");

        List<String> eventHistory = state.getLifeEvents();
        if (eventHistory != null) {
            for (String event : eventHistory) {
                summary.append("- ").append(event).append("\n");
            }
        }

        return summary.toString();
    }

    // 辅助方法
    private List<String> nextNodeByKnowledge(String high, String medium, String low) {
        return Arrays.asList(high, medium, low);
    }

    private List<String> nextNodeByWealthAndKnowledge(String... nodes) {
        return Arrays.asList(nodes);
    }

    private List<String> nextNodeByHappiness(String... nodes) {
        return Arrays.asList(nodes);
    }

    // 初始化游戏状态
    public GameState initializeGame() {
        GameState state = new GameState();
        state.setCurrentNodeId("start");
        state.setCurrentEvent("人生之旅即将开始！请为你的角色命名：");
        state.setCurrentChoices(Arrays.asList("确定名字"));
        state.setLastKeyNode("start");
        return state;
    }
}

// 修改 DecisionNode 类
@Data
class DecisionNode {
    private String id;
    private int age;
    private String description;
    private List<String> choices;
    private List<String> nextNodeIds;
    private List<AttributeEffect> effects;  // 改为效果列表

    public DecisionNode(String id, int age, String description, List<String> choices,
                        List<String> nextNodeIds, List<AttributeEffect> effects) {
        this.id = id;
        this.age = age;
        this.description = description;
        this.choices = choices;
        this.nextNodeIds = nextNodeIds;
        this.effects = effects;
    }

    // 为了向后兼容，添加一个接受单个效果的构造方法
    public DecisionNode(String id, int age, String description, List<String> choices,
                        List<String> nextNodeIds, AttributeEffect singleEffect) {
        this.id = id;
        this.age = age;
        this.description = description;
        this.choices = choices;
        this.nextNodeIds = nextNodeIds;
        this.effects = new ArrayList<>();
        // 为每个选择都应用相同的效果
        for (int i = 0; i < choices.size(); i++) {
            this.effects.add(singleEffect);
        }
    }
}

@Data
class RandomEvent {
    private String id;
    private String description;
    private List<String> choices;
    private AttributeEffect effect;

    public RandomEvent(String id, String description, List<String> choices, AttributeEffect effect) {
        this.id = id;
        this.description = description;
        this.choices = choices;
        this.effect = effect;
    }
}