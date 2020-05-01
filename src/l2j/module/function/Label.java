package l2j.module.function;

public class Label {
	public String destinationName;
	public BasicBlock destination;
	private Function f;

	public Label(String destinationName, Function f) {
		this.destinationName = destinationName;
		this.f = f;
	}

	public void resolve() {
		destination = f.blocks.get(destinationName);
		if (destination == null)
			throw new IllegalStateException("not found: destination block");
	}
}
