package be.vdab;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 *
 */
@Theme("mytheme")
@Widgetset("be.vdab.MyAppWidgetset")
public class MyUI extends UI {

    private Label lab = new Label();
    private int tries = 3;
    private int guess = generate();
    Label label = new Label("Guess the number (answer = 2)");
    TextField textField = new TextField();
    Button button = new Button("Random number");
    Button reset = new Button("Reset");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        textField.setConverter(new StringToIntegerConverter());
        textField.addValidator(new IntegerRangeValidator("Only Integers allowed!", 1, 10));
        textField.setNullRepresentation("");


        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    textField.validate();
                    Integer value = (Integer) textField.getConvertedValue();
                    int answer = guess;
                    if (value == answer) {
                        lab.setCaption("You won");
                        layout.replaceComponent(button, reset);
                    } else {
                        tries = tries - 1;
                        if (tries == 0) {
                            lab.setCaption("You lose number was " + answer);
                            layout.replaceComponent(button, reset);
                        } else {
                            lab.setCaption("Try again, guesses left : " + tries);
                        }
                    }
                } catch (Validator.InvalidValueException e) {
                    e.printStackTrace();
                }
            }
        });

        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                layout.replaceComponent(reset, button);
                textField.setValue("");
                guess = generate();
                tries = 3;
            }
        });

        layout.addComponent(textField);
        layout.addComponent(button);
        layout.addComponent(label);
        layout.addComponent(lab);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private int generate() {
        return (int) (Math.round(Math.random() * (10 - 1) + 1));
    }
}
