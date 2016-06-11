package Spark.Server;

import static spark.Spark.*;
import spark.Route;

public class StartServer {
    public static void main(String[] args) {
        get("hello", ((request, response) -> "Ola"));
    }
}


