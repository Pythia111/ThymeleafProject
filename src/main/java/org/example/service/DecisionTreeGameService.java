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
        // 重新设计关键节点年龄，使其符合现实逻辑
        keyNodeAges.put("start", 0);
        keyNodeAges.put("naming", 0);
        keyNodeAges.put("attribute_allocation", 0);
        keyNodeAges.put("infant_1", 1);
        keyNodeAges.put("childhood_1", 6);
        keyNodeAges.put("teenager_1", 13);
        keyNodeAges.put("high_school_graduation", 18);
        keyNodeAges.put("college_graduation", 22);
        keyNodeAges.put("career_start", 25);
        keyNodeAges.put("midlife_crisis", 45);
        keyNodeAges.put("retirement_decision", 65);
        keyNodeAges.put("game_end", 80);
    }

    private void buildRandomEvents() {
        // 重新设计随机事件，确保年龄阶段合理

        // 婴幼儿期随机事件 (1-5岁)
        randomEvents.add(new RandomEvent("infant_random_1",
                "你学会了爬行，探索家里的每个角落",
                Arrays.asList("继续探索", "休息一下"),
                new AttributeEffect(0, 5, 3, 2)));

        randomEvents.add(new RandomEvent("infant_random_2",
                "你第一次开口说话，父母非常高兴",
                Arrays.asList("多说几句话", "害羞地躲起来"),
                new AttributeEffect(0, 2, 8, 5)));

        // 童年期随机事件 (6-12岁)
        randomEvents.add(new RandomEvent("childhood_random_1",
                "你在学校交到了新朋友",
                Arrays.asList("一起玩耍", "邀请来家里做客"),
                new AttributeEffect(0, 3, 10, 2)));

        randomEvents.add(new RandomEvent("childhood_random_2",
                "你在运动会上表现出色",
                Arrays.asList("继续训练", "尝试其他运动"),
                new AttributeEffect(0, 8, 5, 0)));

        randomEvents.add(new RandomEvent("childhood_random_3",
                "你在考试中取得了好成绩",
                Arrays.asList("继续努力", "放松一下"),
                new AttributeEffect(0, 0, 8, 10)));

        // 青少年期随机事件 (13-17岁)
        randomEvents.add(new RandomEvent("teenager_random_1",
                "你参加了学校的社团活动",
                Arrays.asList("积极参与", "观望一下"),
                new AttributeEffect(0, 3, 8, 5)));

        randomEvents.add(new RandomEvent("teenager_random_2",
                "你遇到了青春期的烦恼",
                Arrays.asList("向父母倾诉", "自己解决"),
                new AttributeEffect(0, -3, -5, 2)));

        randomEvents.add(new RandomEvent("teenager_random_3",
                "你在学科竞赛中获奖",
                Arrays.asList("继续努力", "尝试新领域"),
                new AttributeEffect(0, 2, 12, 15)));

        // 大学期随机事件 (18-24岁)
        randomEvents.add(new RandomEvent("college_random_1",
                "你在大学里选择了专业方向",
                Arrays.asList("专注学术研究", "参与社会实践"),
                new AttributeEffect(0, 0, 5, 10)));

        randomEvents.add(new RandomEvent("college_random_2",
                "你参加了实习工作",
                Arrays.asList("努力工作积累经验", "探索其他可能性"),
                new AttributeEffect(5, -2, 3, 8)));

        randomEvents.add(new RandomEvent("college_random_3",
                "你遇到了心仪的对象",
                Arrays.asList("主动追求", "顺其自然"),
                new AttributeEffect(0, 0, 15, 0)));

        // 青年期随机事件 (25-44岁)
        randomEvents.add(new RandomEvent("youth_random_1",
                "你在工作中遇到挑战",
                Arrays.asList("积极应对", "寻求帮助"),
                new AttributeEffect(0, -5, -3, 8)));

        randomEvents.add(new RandomEvent("youth_random_2",
                "你获得了晋升机会",
                Arrays.asList("接受挑战", "保持现状"),
                new AttributeEffect(15, -5, 5, 5)));

        randomEvents.add(new RandomEvent("youth_random_3",
                "家庭生活带来幸福感",
                Arrays.asList("多陪伴家人", "专注事业发展"),
                new AttributeEffect(0, 5, 12, 0)));

        // 中年期随机事件 (45-64岁)
        randomEvents.add(new RandomEvent("middle_random_1",
                "你的工作压力增大",
                Arrays.asList("加班应对", "调整工作方式"),
                new AttributeEffect(5, -10, -8, 3)));

        randomEvents.add(new RandomEvent("middle_random_2",
                "健康检查发现问题",
                Arrays.asList("积极治疗", "忽视问题"),
                new AttributeEffect(-10, -15, -5, 0)));

        randomEvents.add(new RandomEvent("middle_random_3",
                "孩子教育成为关注重点",
                Arrays.asList("投入更多精力", "保持现状"),
                new AttributeEffect(-5, 0, 8, 5)));

        // 老年期随机事件 (65岁及以上)
        randomEvents.add(new RandomEvent("elder_random_1",
                "你开始享受退休生活",
                Arrays.asList("发展新爱好", "安享晚年"),
                new AttributeEffect(0, 5, 10, 3)));

        randomEvents.add(new RandomEvent("elder_random_2",
                "健康状况需要关注",
                Arrays.asList("定期检查", "顺其自然"),
                new AttributeEffect(-5, -8, -3, 0)));

        randomEvents.add(new RandomEvent("elder_random_3",
                "与家人共度美好时光",
                Arrays.asList("多组织家庭聚会", "享受宁静"),
                new AttributeEffect(0, 3, 15, 2)));
    }

    private void buildDecisionTree() {
        // === 人生起点 ===
        decisionTree.put("start", new DecisionNode(
                "start", 0,
                "人生之旅即将开始！请为你的角色命名：",
                Arrays.asList("确定名字"),
                Arrays.asList("naming"),
                null
        ));

        // === 命名阶段 ===
        decisionTree.put("naming", new DecisionNode(
                "naming", 0,
                "很好！现在请分配你的初始属性（共20点）：",
                Arrays.asList("进入属性分配"),
                Arrays.asList("attribute_allocation"),
                null
        ));

        // === 属性分配完成 ===
        decisionTree.put("attribute_allocation", new DecisionNode(
                "attribute_allocation", 0,
                "属性分配完成！你的人生正式开始...",
                Arrays.asList("开始人生旅程"),
                Arrays.asList("infant_1"),
                null
        ));

        // === 婴幼儿期关键节点 ===
        decisionTree.put("infant_1", new DecisionNode(
                "infant_1", 1,
                "作为婴儿，你的早期发展特点是...",
                Arrays.asList("身体强壮好动", "聪明早慧爱学习", "活泼开朗爱社交"),
                Arrays.asList("childhood_1", "childhood_1", "childhood_1"),
                new AttributeEffect(0, 5, 8, 5)
        ));

        // === 童年期关键节点 ===
        decisionTree.put("childhood_1", new DecisionNode(
                "childhood_1", 6,
                "进入小学，你的学习态度是...",
                Arrays.asList("努力学习争取好成绩", "发展兴趣爱好", "快乐玩耍享受童年"),
                nextNodeByKnowledge("teenager_1", "teenager_1", "teenager_1"),
                new AttributeEffect(0, 3, 5, 8)
        ));

        // === 青少年期关键节点 ===
        decisionTree.put("teenager_1", new DecisionNode(
                "teenager_1", 13,
                "进入中学，面临学业压力，你选择...",
                Arrays.asList("专注学习备战考试", "平衡学习与兴趣", "享受青春时光"),
                nextNodeByKnowledge("high_school_graduation", "high_school_graduation", "high_school_graduation"),
                new AttributeEffect(0, 0, 5, 10)
        ));

        // === 高中毕业关键节点 ===
        decisionTree.put("high_school_graduation", new DecisionNode(
                "high_school_graduation", 18,
                "高中毕业，面临人生重要选择...",
                Arrays.asList("参加高考上大学", "学习技术就业", "直接工作积累经验"),
                nextNodeByKnowledge("college_graduation", "career_start", "career_start"),
                new AttributeEffect(0, -5, 0, 5)
        ));

        // === 大学毕业关键节点 ===
        decisionTree.put("college_graduation", new DecisionNode(
                "college_graduation", 22,
                "大学毕业，就业选择...",
                Arrays.asList("大公司稳定工作", "创业追求梦想", "继续深造读研"),
                Arrays.asList("career_start", "career_start", "career_start"),
                new AttributeEffect(10, 0, 5, 5)
        ));

        // === 职业生涯开始关键节点 ===
        decisionTree.put("career_start", new DecisionNode(
                "career_start", 25,
                "职业生涯正式开始，你注重...",
                Arrays.asList("努力工作晋升", "工作生活平衡", "积累人脉资源"),
                Arrays.asList("midlife_crisis", "midlife_crisis", "midlife_crisis"),
                new AttributeEffect(15, -3, 5, 3)
        ));

        // === 中年危机关键节点 ===
        decisionTree.put("midlife_crisis", new DecisionNode(
                "midlife_crisis", 45,
                "步入中年，面临生活压力，你选择...",
                Arrays.asList("专注事业发展", "重视家庭生活", "寻求生活改变"),
                nextNodeByHappiness("retirement_decision", "retirement_decision", "retirement_decision"),
                new AttributeEffect(5, -5, 0, 3)
        ));

        // === 退休决策关键节点 ===
        decisionTree.put("retirement_decision", new DecisionNode(
                "retirement_decision", 65,
                "到了退休年龄，你决定...",
                Arrays.asList("正式退休安享晚年", "继续工作发挥余热", "半退休享受生活"),
                Arrays.asList("game_end", "game_end", "game_end"),
                new AttributeEffect(0, 5, 10, 0)
        ));

        // === 游戏结束 ===
        decisionTree.put("game_end", new DecisionNode(
                "game_end", 80,
                "人生旅程结束",
                Arrays.asList("查看人生总结", "开始新游戏"),
                Arrays.asList("life_summary", "new_game"),
                new AttributeEffect(0, 0, 0, 0)
        ));
    }

    // 获取随机事件 - 修复版本
    private RandomEvent getRandomEvent(String currentKeyNode, String nextKeyNode, GameState state) {
        List<RandomEvent> availableEvents = new ArrayList<>();

        int currentAge = keyNodeAges.get(currentKeyNode);
        int nextAge = keyNodeAges.get(nextKeyNode);

        for (RandomEvent event : randomEvents) {
            String eventId = event.getId();

            // 根据事件ID前缀和当前年龄阶段筛选合适的事件
            if (eventId.contains("infant") && currentAge >= 1 && currentAge < 6) {
                availableEvents.add(event);
            } else if (eventId.contains("childhood") && currentAge >= 6 && currentAge < 13) {
                availableEvents.add(event);
            } else if (eventId.contains("teenager") && currentAge >= 13 && currentAge < 18) {
                availableEvents.add(event);
            } else if (eventId.contains("college") && currentAge >= 18 && currentAge < 25) {
                availableEvents.add(event);
            } else if (eventId.contains("youth") && currentAge >= 25 && currentAge < 45) {
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

    // 获取两个关键节点之间的随机年龄 - 修复版本
    private int getRandomAgeBetween(String currentKeyNode, String nextKeyNode) {
        int minAge = keyNodeAges.get(currentKeyNode);
        int maxAge = keyNodeAges.get(nextKeyNode);

        // 确保年龄严格递增且合理
        if (maxAge <= minAge) {
            return minAge + 1;
        }

        return minAge + 1 + random.nextInt(maxAge - minAge - 1);
    }

    // 核心处理方法 - 修复版本
    public GameState processDecision(GameState state, int choiceIndex) {
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
        }

        DecisionNode currentNode = decisionTree.get(currentNodeId);
        if (currentNode == null) {
            currentNode = decisionTree.get("start");
        }

        if (choiceIndex >= 0 && choiceIndex < currentNode.getNextNodeIds().size()) {
            String nextNodeId = currentNode.getNextNodeIds().get(choiceIndex);

            // 检查是否是关键节点之间的过渡
            if (isKeyNode(currentNodeId) && isKeyNode(nextNodeId) &&
                    !currentNodeId.equals(nextNodeId)) {

                // 在关键节点之间插入随机事件
                RandomEvent randomEvent = getRandomEvent(currentNodeId, nextNodeId, state);
                if (randomEvent != null && random.nextDouble() < 0.6) { // 60%概率触发随机事件
                    return handleRandomEvent(state, randomEvent, nextNodeId);
                }
            }

            return moveToNextNode(state, currentNode, nextNodeId, choiceIndex);
        }

        return state;
    }

    private boolean isKeyNode(String nodeId) {
        return keyNodeAges.containsKey(nodeId);
    }

    private GameState handleRandomEvent(GameState state, RandomEvent randomEvent, String nextKeyNodeId) {
        // 设置随机事件信息
        state.setCurrentNodeId("random_event");
        state.setCurrentEvent(randomEvent.getDescription());
        state.setCurrentChoices(randomEvent.getChoices());
        state.setPendingKeyNode(nextKeyNodeId);
        state.setPendingRandomEvent(randomEvent);

        // 更新年龄（在关键节点年龄之间随机）
        int randomAge = getRandomAgeBetween(state.getLastKeyNode(), nextKeyNodeId);
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

            // 记录事件 - 修复重复年龄问题
            state.addEvent(randomEvent.getDescription() + " → " + randomEvent.getChoices().get(choiceIndex));

            // 检查游戏结束条件
            if (checkGameEndCondition(state)) {
                return state;
            }

            // 移动到下一个关键节点
            DecisionNode lastKeyNode = decisionTree.get(state.getLastKeyNode());
            return moveToNextNode(state, lastKeyNode, nextKeyNodeId, 0);
        }

        return state;
    }

    private GameState moveToNextNode(GameState state, DecisionNode currentNode, String nextNodeId, int choiceIndex) {
        DecisionNode nextNode = decisionTree.get(nextNodeId);

        // 应用属性效果
        Object effectObj = currentNode.getEffect();
        if (effectObj instanceof AttributeEffect) {
            AttributeEffect effect = (AttributeEffect) effectObj;
            state.applyEffect(effect.getWealthChange(), effect.getHealthChange(),
                    effect.getHappinessChange(), effect.getKnowledgeChange());
        }

        // 更新年龄
        if (isKeyNode(nextNodeId)) {
            state.setAge(keyNodeAges.get(nextNodeId));
            state.setLastKeyNode(nextNodeId);
        }

        // 记录事件（关键节点）- 修复重复年龄问题
        if (isKeyNode(currentNode.getId()) && !currentNode.getId().equals("start") &&
                !currentNode.getId().equals("naming") && !currentNode.getId().equals("attribute_allocation")) {
            state.addEvent(currentNode.getDescription() + " → " + currentNode.getChoices().get(choiceIndex));
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

@Data
class DecisionNode {
    private String id;
    private int age;
    private String description;
    private List<String> choices;
    private List<String> nextNodeIds;
    private Object effect;

    public DecisionNode(String id, int age, String description, List<String> choices,
                        List<String> nextNodeIds, Object effect) {
        this.id = id;
        this.age = age;
        this.description = description;
        this.choices = choices;
        this.nextNodeIds = nextNodeIds;
        this.effect = effect;
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