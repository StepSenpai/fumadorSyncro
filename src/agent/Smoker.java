package agent;


import java.util.concurrent.Semaphore;

public class Smoker implements Runnable {

    private Table table;
    private Component component;
    private Semaphore components;

    public Smoker(Table table, Component component) {
        this.table = table;
        this.component = component;
        this.components = new Semaphore(0);
    }

    public Component getComponent() {
        return component;
    }

    public boolean hasComponent(Component component) {
        return this.component == component;
    }

    public void smoke() throws InterruptedException {
        System.out.println("Smoker " + component + " is smoking");
        Thread.sleep(3000);
        System.out.println("Smoker " + component + " stopped smoking");
    }

    public void awake() {
        components.release();
    }

    private void awaitComponents() throws InterruptedException {
        components.acquire();
    }

    public void run() {
        while (true) {
            try {
                awaitComponents();
                smoke();
                table.awakeAgent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
