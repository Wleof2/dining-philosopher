module com.dijkstra.diningphilosophers {
  requires javafx.controls;
  requires javafx.fxml;

  opens com.dijkstra.diningphilosophers to javafx.fxml;
  exports com.dijkstra.diningphilosophers;
  exports com.dijkstra.diningphilosophers.model;
  opens com.dijkstra.diningphilosophers.model to javafx.fxml;
  exports com.dijkstra.diningphilosophers.model.worker;
  opens com.dijkstra.diningphilosophers.model.worker to javafx.fxml;
  exports com.dijkstra.diningphilosophers.model.dinner;
  opens com.dijkstra.diningphilosophers.model.dinner to javafx.fxml;
}