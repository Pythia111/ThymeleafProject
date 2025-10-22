package org.example.controller;

import org.example.entity.User;
import org.example.entity.GameRecord;
import org.example.dao.GameRecordRepository;
import org.example.dao.UserRepository;
import org.example.service.DecisionTreeGameService;
import org.example.service.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private DecisionTreeGameService decisionTreeGameService;

    @Autowired
    private GameRecordRepository gameRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/decision")
    public String processDecision(@RequestParam int choiceIndex, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        GameState gameState = (GameState) session.getAttribute("gameState");
        if (gameState == null) return "redirect:/game/new";

        // 使用决策树处理选择
        decisionTreeGameService.processDecision(gameState, choiceIndex);

        // 如果游戏结束，保存记录
        if (gameState.isGameEnded()) {
            saveGameRecord(user, gameState);
        }

        return "redirect:/game";
    }

    // 新增：保存游戏记录的方法
    private void saveGameRecord(User user, GameState gameState) {
        GameRecord record = new GameRecord();
        record.setUser(user);
        record.setCharacterName(gameState.getCharacterName());
        record.setFinalAge(gameState.getAge());
        record.setFinalWealth(gameState.getWealth());
        record.setFinalHealth(gameState.getHealth());
        record.setFinalHappiness(gameState.getHappiness());
        record.setFinalKnowledge(gameState.getKnowledge());
        record.setEndingType(generateEnding(gameState));
        record.setTotalEvents(gameState.getLifeEvents().size());
        record.setLifePath(String.join("|", gameState.getLifeEvents()));

        gameRecordRepository.save(record);
    }

    private String generateEnding(GameState state) {
        if (state.getHealth() <= 0) return "因健康问题过早离世";
        if (state.getWealth() >= 80 && state.getKnowledge() >= 70) return "成功的企业家兼学者";
        if (state.getWealth() >= 80) return "富有的商业精英";
        if (state.getKnowledge() >= 80) return "受人尊敬的学者";
        if (state.getHappiness() >= 80) return "快乐满足的平凡人生";
        if (state.getHealth() >= 80) return "健康长寿的幸福人生";
        return "平凡而真实的一生";
    }

    // 更新游戏页面，使用决策树
    @GetMapping
    public String gamePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        GameState gameState = (GameState) session.getAttribute("gameState");
        if (gameState == null) {
            return "redirect:/game/new";
        }

        // 如果是新游戏，初始化决策树
        if (gameState.getCurrentNodeId() == null) {
            gameState.setCurrentNodeId("start");
            gameState.setCurrentEvent("人生之旅即将开始！请为你的角色命名：");
            gameState.setCurrentChoices(Arrays.asList("确定名字"));
        }

        model.addAttribute("gameState", gameState);
        return "game";
    }

    @GetMapping("/new")
    public String newGame(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // 使用决策树服务初始化游戏状态
        GameState gameState = decisionTreeGameService.initializeGame();
        gameState.setCharacterName(user.getUsername() + "的人生");
        session.setAttribute("gameState", gameState);

        return "redirect:/game";
    }

    // 新增：历史记录页面
    @GetMapping("/records")
    public String recordsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<GameRecord> records = gameRecordRepository.findByUserIdOrderByPlayTimeDesc(user.getId());
        model.addAttribute("records", records);
        return "records";
    }

    // 新增：处理属性分配
    @GetMapping("/allocate")
    public String allocatePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        GameState gameState = (GameState) session.getAttribute("gameState");
        if (gameState == null) {
            return "redirect:/game/new";
        }

        model.addAttribute("gameState", gameState);
        return "attribute-allocation";
    }

    @PostMapping("/choice")
    public String handleChoice(@RequestParam(required = false) Integer wealth,
                               @RequestParam(required = false) Integer health,
                               @RequestParam(required = false) Integer happiness,
                               @RequestParam(required = false) Integer knowledge,
                               @RequestParam(required = false) Integer choiceIndex,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        GameState gameState = (GameState) session.getAttribute("gameState");
        if (gameState == null) return "redirect:/game/new";

        // 处理初始属性分配
        if (!gameState.isAttributesAllocated() &&
                wealth != null && health != null && happiness != null && knowledge != null) {

            // 验证点数分配
            if (wealth + health + happiness + knowledge != 20) {
                return "redirect:/game";
            }

            // 应用属性分配
            gameState.setWealth(10 + wealth);
            gameState.setHealth(10 + health);
            gameState.setHappiness(10 + happiness);
            gameState.setKnowledge(10 + knowledge);
            gameState.setAttributesAllocated(true);

            // 使用决策树服务处理初始节点跳转
            decisionTreeGameService.processDecision(gameState, 0); // 从 start 开始
            return "redirect:/game";
        }

        // 处理游戏中的选择
        if (choiceIndex != null) {
            // 使用决策树处理选择
            decisionTreeGameService.processDecision(gameState, choiceIndex);

            // 如果游戏结束，保存记录
            if (gameState.isGameEnded()) {
                saveGameRecord(user, gameState);
            }
        } else {
            // 处理初始选择（如"确定名字"、"进入属性分配"等）
            decisionTreeGameService.processDecision(gameState, 0);
        }

        return "redirect:/game";
    }

    // 新增：处理提前退出游戏
    @PostMapping("/exit")
    public String exitGame(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        GameState gameState = (GameState) session.getAttribute("gameState");
        if (gameState != null && !gameState.isGameEnded()) {
            // 保存未完成的游戏记录
            saveGameRecord(user, gameState);
            session.removeAttribute("gameState");
        }

        return "redirect:/records";
    }
}