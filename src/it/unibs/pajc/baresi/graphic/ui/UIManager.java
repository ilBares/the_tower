package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.graphic.Screen;
import it.unibs.pajc.baresi.level.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private List<UIButton> buttons = new ArrayList<>();
    private UIText lblMoney;

    public UIManager(int screenWidth) {
        UIButton btnMob = UIButton.btnMob;
        UIButton btnBack = UIButton.btnBack;

        buttons.add(btnMob);
        buttons.add(btnBack);
        buttons.add(UIButton.btnMiniGolem);
        buttons.add(UIButton.btnAdventurer);
        buttons.add(UIButton.btnDragon);
        buttons.add(UIButton.btnGolem);

        btnMob.setVisible(true);

        btnMob.setUiButtonListener(new UIButtonListener() {
            @Override
            public void released(UIButton button) {
                BufferedImage image = button.getImage();
                image.setRGB(0, 0, image.getWidth(), image.getHeight(), button.getDefaultRGB(), 0, image.getWidth());
                btnMob.setVisible(false);
                buttons.forEach(b -> b.setVisible(true));
            }
        });

        btnBack.setUiButtonListener(new UIButtonListener() {
            @Override
            public void released(UIButton button) {
                BufferedImage image = button.getImage();
                image.setRGB(0, 0, image.getWidth(), image.getHeight(), button.getDefaultRGB(), 0, image.getWidth());
                buttons.stream().skip(1).forEach(b -> b.setVisible(false));
                btnMob.setVisible(true);
            }
        });

        lblMoney = new UIText("", screenWidth - 150, 75, 40);
    }

    public void update() {
        buttons.forEach(UIButton::update);
    }

    public void render(Graphics2D g2, Screen screen, String money) {
        // rendering UI Buttons
        for (UIButton btn : buttons) {
            if (btn.isVisible()) {
                screen.drawUIButton(g2, btn);
                // c.render(g2);
            }
        }

        // components.stream().filter(UIButton::isVisible).forEach(btn -> btn.render(g2));
        buttons.stream().filter(UIButton::isVisible).forEach(btn -> screen.drawUIButton(g2, btn));

        lblMoney.setText(money);
        screen.drawUIText(g2, lblMoney);
    }
}
