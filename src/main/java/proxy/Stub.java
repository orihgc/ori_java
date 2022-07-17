package proxy;

public class Stub implements IStub {
    @Override
    public void action() {
        System.out.println("Stub action");
    }
}
