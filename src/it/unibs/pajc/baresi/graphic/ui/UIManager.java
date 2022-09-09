package it.unibs.pajc.baresi.graphic.ui;

import it.unibs.pajc.baresi.graphic.Screen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * UIManager of the game.
 * It handles buttons and labels.
 *
 * @see UIButton
 * @see UIText
 */
public class UIManager {
    private List<UIButton> buttons = new ArrayList<>();
    private UIText lblMoney;


    ///
    /// Constructor
    ///
    public UIManager(int screenWidth) {
        UIButton btnMob = UIButton.btnMob;
        UIButton btnBack = UIButton.btnBack;

        // adding different buttons
        buttons.add(btnMob);
        buttons.add(btnBack);
        buttons.add(UIButton.btnMiniGolem);
        buttons.add(UIButton.btnAdventurer);
        buttons.add(UIButton.btnDragon);
        buttons.add(UIButton.btnGolem);

        btnMob.setVisible(true);

        // Setting mob button listener
        btnMob.setUiButtonListener(new UIButtonListener() {
            @Override
            public void released(UIButton button) {
                BufferedImage image = button.getImage();
                image.setRGB(0, 0, image.getWidth(), image.getHeight(), button.getDefaultRGB(), 0, image.getWidth());
                btnMob.setVisible(false);
                buttons.forEach(b -> b.setVisible(true));
            }
        });

        // Setting back button listener
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


    ///
    /// Updating and Rendering
    ///

    /**
     * Updates each button.
     */
    public void update() {
        buttons.forEach(UIButton::update);
    }

    /**
     * Renders each visible button
     * @param g2        Graphics 2D object
     * @param screen    screen
     * @param money
     */
    public void render(Graphics2D g2, Screen screen, String money) {
        // rendering UI Buttons
        for (UIButton btn : buttons) {
            if (btn.isVisible()) {
                screen.drawUIButton(g2, btn);
            }
        }

        // renders each visible button
        buttons.stream().filter(UIButton::isVisible).forEach(btn -> screen.drawUIButton(g2, btn));

        // renders money label
        lblMoney.setText(money);
        screen.drawUIText(g2, lblMoney);
    }
}
