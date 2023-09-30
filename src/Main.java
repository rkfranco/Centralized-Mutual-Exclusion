public class Main {
    public static void main(String[] args) {
        new CentralizedMutualExclusion().start();
    }
}

/*
- a cada 1 minuto o coordenador morre
- quando o coordenador morre, a fila também morre
- o tempo de processamento de um recurso é de 5 a 15 segundos
- os processos tentam consumir o(s) recurso(s) num intervalo de 10 a 25 segundos
- a cada 40 segundos um novo processo deve ser criado (ID randômico)
- dois processos não podem ter o mesmo ID
* */