package src.other;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class RootLayoutController {

	@FXML
	private Label label;
	@FXML
	private Slider slider;

	public RootLayoutController() {
		// Do nothing
	}

	public void init() {
		StringConverter<Number> converter = new NumberStringConverter();
		Bindings.bindBidirectional(this.label.textProperty(), this.slider.valueProperty(), converter);
	}

	@FXML
	private void moreButtonHandler() {
		this.slider.setValue(this.slider.getValue() + 0.1);
	}

	@FXML
	private void lessButtonHandler() {
		this.slider.setValue(this.slider.getValue() - 0.1);
	}

	@FXML
	private void saveButtonHandler() {
		double value = this.slider.getValue();
		// Do whatever you want to do !
	}

}
