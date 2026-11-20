import java.util.ArrayList;
import java.util.List;

public class Lista {
    private List<Paqueteria> paquetes;

    public Lista() {
        paquetes = new ArrayList<Paqueteria>();
    }

    public List<Paqueteria> paqueterias() {
        return paquetes;
    }

    public boolean isEmpty() {
        return paquetes.isEmpty();
    }


    public void validarTracking(int tracking) throws Exception {
        for (Paqueteria p : paquetes) {
            if (p.getTracking() == tracking) {
                throw new Exception("Ya existe un paquete con el tracking " + tracking);
            }
        }
    }

    //Registrar un paquete
    public void registrarPaquete(Paqueteria p) throws Exception {
        validarTracking(p.getTracking());
        paquetes.add(p);
    }

    //Listar paquetes
    public String listarPaquetes() {
        String resultado = "";
        for (Paqueteria p : paquetes) {
            resultado += p + "\n";
        }
        return resultado;
    }

    //Buscar paquetes
    public Paqueteria buscarPaquete(int tracking) throws Exception {
        for(Paqueteria p : paquetes){
            if(p.getTracking() == tracking){
                return p;
            }
        }
        throw new Exception("No existe un paquete con el tracking " +tracking);
    }


    //Modificar informacion
    public void modificarPaquete(int tracking, double peso, String ciudadRecepcion, String ciudadEntrega, String cedulaReceptor) throws Exception {
        Paqueteria p = buscarPaquete(tracking);
        p.setPeso(peso);
        p.setCiudadRecepcion(ciudadRecepcion);
        p.setCiudadEntrega(ciudadEntrega);
        p.setCedulaReceptor(cedulaReceptor);
    }

    //Actualizar estado: Receptado -> Enviado -> Recibido
    public String actualizarEstado(int tracking) throws Exception {
        Paqueteria p = buscarPaquete(tracking);
        if (p.getEstado().equals("Receptado")) {
            p.setEstado("Enviado");
            return "Enviado";
        } else if (p.getEstado().equals("Enviado")) {
            p.setEstado("Recibido");
            return "Recibido";
        } else {
            return "Finalizado";
        }
    }

    //Recursividad siempre recibe parametro y ese parametro que se recibe deberia irse modificando

    //Calcular el número total de paquetes registrados
    public int totalPaquetes(){
        return totalPaquetesRecursivo(0);
    }
    private int totalPaquetesRecursivo(int indice){
        if(indice < paquetes.size()){
            return 1 + totalPaquetesRecursivo(indice+1);
        }
        return 0;
    }

    //Calcular la suma de los pesos de todos los paquetes
    public double totalPesos(){
        return totalPesosRecursivo(0);
    }
    private double totalPesosRecursivo(int indice){
        if(indice < paquetes.size()){
            return paquetes.get(indice).getPeso() + totalPesosRecursivo(indice+1);
        }
        return 0;
    }

    //Calcular el peso total de los paquetes recibidos en una ciudad específica
    public double totalPesoCiudad(String ciudad){
        return totalPesoCiudadRecursivo(ciudad, 0);
    }
    private double totalPesoCiudadRecursivo(String ciudad, int indice){
        if(indice < paquetes.size()){
            if(paquetes.get(indice).getCiudadRecepcion().equals(ciudad)){
                return paquetes.get(indice).getPeso() + totalPesoCiudadRecursivo(ciudad, indice+1);
            }
            return totalPesoCiudadRecursivo(ciudad, indice+1);
        }
        return 0;
    }

    //Determinar cuántos paquetes se encuentran en un estado determinado
    public int totalPaquetesEstado(String estado){
        return totalPaquetesEstadoRecursivo(estado, 0);
    }
    private int totalPaquetesEstadoRecursivo(String estado, int indice){
        if(indice < paquetes.size()){
            if(paquetes.get(indice).getEstado().equals(estado)){
                return 1 + totalPaquetesEstadoRecursivo(estado, indice+1);
            }
            return totalPaquetesEstadoRecursivo(estado, indice+1);
        }
        return 0;
    }

    //ORDENAMIENTO

    //1. Burbuja: ordena de forma ascendente según el tracking
    public List<Paqueteria> ordenarPaquetesPorTracking(){
        List<Paqueteria> lista = new ArrayList<Paqueteria>();
        for (Paqueteria p : paquetes) {
            lista.add(p);
        }
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lista.get(j).getTracking() > lista.get(j + 1).getTracking()) {
                    // Intercambiar lista[j] y lista[j + 1]
                    Paqueteria temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
        return lista;
    }

    //2. Inserción: ordena de forma ascendente según el peso
    public List<Paqueteria> ordenarPaquetesPorPeso(){
        List<Paqueteria> lista = new ArrayList<Paqueteria>();
        for (Paqueteria p : paquetes) {
            lista.add(p);
        }
        int n = lista.size();
        for (int i = 1; i < n; ++i) {
            Paqueteria key = lista.get(i);
            int j = i - 1;
            // Desplaza los paquetes de lista[0..i-1] que pesan más que key
            while (j >= 0 && lista.get(j).getPeso() > key.getPeso()) {
                lista.set(j + 1, lista.get(j));
                j = j - 1;
            }
            // Inserta el key en su posición correcta
            lista.set(j + 1, key);
        }
        return lista;
    }

    //BÚSQUEDAS

    //1. Búsqueda binaria: recibe la lista ordenada por tracking y devuelve la posición o -1
    public int busquedaBinaria(List<Paqueteria> lista, int tracking){
        int inicio = 0;
        int fin = lista.size() - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            if (lista.get(medio).getTracking() == tracking) {
                return medio; // Retorna la posición del paquete encontrado
            } else if (lista.get(medio).getTracking() < tracking) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1; // Retorna -1 si el tracking no se encuentra
    }

    //2. Búsqueda por criterios combinados: cédula del receptor y estado
    public List<Paqueteria> buscarPaquetesPorCedulaYEstado(String cedula, String estado){
        List<Paqueteria> resultado = new ArrayList<Paqueteria>();
        for (Paqueteria p : paquetes) {
            if (p.getCedulaReceptor().equals(cedula) && p.getEstado().equals(estado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

}

