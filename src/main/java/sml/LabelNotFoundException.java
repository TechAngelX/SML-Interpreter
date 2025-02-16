package sml;

public class LabelNotFoundException extends RuntimeException {
    private final Label label;
    private final Method method;

    public LabelNotFoundException(Label label, Method method) {
        super("Label " + label + " not found in " + method.name());
        this.label = label;
        this.method = method;
    }

    public Label getLabel() {
        return label;
    }

    public Method getMethod() {
        return method;
    }
}