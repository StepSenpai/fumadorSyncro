class Mesa {
    boolean tabaco;
    boolean papel;
    boolean notificar; // Usado para el agente, para notificar.

    public Mesa() {
        // Configuración inicial: nada en la mesa y notificación al agente
        this.tabaco = false;
        this.papel = false;
        this.notificar = true;
    }
}

class Agente extends Thread {
    Mesa mesa;

    public Agente(Mesa mesa) {
        this.mesa = mesa;
    }

    public void alertar() {
        synchronized (mesa) {
            System.out.println("Agente alertado..");
            mesa.notificar = true;
            mesa.notifyAll();
        }
    }

    public void run() {
        try {
            synchronized (mesa) {
                while (true) {
                    while (!mesa.notificar) {
                        System.out.println("El agente está observando..");
                        mesa.wait();
                    }
                    System.out.println("El agente coloca tabaco y papel en la mesa");
                    mesa.tabaco = true;
                    mesa.papel = true;
                    mesa.notificar = false;
                    mesa.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            // Manejar interrupción
        }
    }
}

class Fumador extends Thread {
    String[] etapas = {"Armando", "Encendiendo", "Fumando", "Apagando"};
    Mesa mesa;
    Agente agente;
    int id;

    public Fumador(Mesa mesa, Agente agente, int id) {
        this.mesa = mesa;
        this.agente = agente;
        this.id = id;
    }

    public void run() {
        try {
            synchronized (mesa) {
                while (true) {
                    while (!(mesa.tabaco && mesa.papel)) {
                        System.out.println("Fumador #" + id + " esperando...");
                        mesa.wait();
                    }
                    if (mesa.tabaco && mesa.papel) {
                        mesa.papel = false;
                        mesa.tabaco = false;
                        System.out.print("Fumador #" + id + " está: ");
                        for (int i = 0; i < 4; i++) {
                            System.out.print(etapas[i] + " ");
                            Thread.sleep(250);
                        }
                        System.out.println();
                        agente.alertar();
                    }
                }
            }
        } catch (InterruptedException e) {
            // Manejar interrupción
        }
    }
}

public class MainProblema2 {
    public static void main(String[] args) {
        Mesa mesa = new Mesa();
        Agente agente = new Agente(mesa);
        Fumador f1 = new Fumador(mesa, agente, 1);
        Fumador f2 = new Fumador(mesa, agente, 2);
        Fumador f3 = new Fumador(mesa, agente, 3);

        agente.start();
        f1.start();
        f2.start();
        f3.start();
    }
}
