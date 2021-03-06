package engine.enginemod.client.gui.game;

import engine.Platform;
import engine.client.EngineClientImpl;
import engine.game.GameData;
import engine.game.GameDataStorage;
import engine.graphics.font.Font;
import engine.gui.Scene;
import engine.gui.control.*;
import engine.gui.input.MouseActionEvent;
import engine.gui.layout.BorderPane;
import engine.gui.layout.HBox;
import engine.gui.layout.VBox;
import engine.gui.misc.*;
import engine.gui.stage.Popup;
import engine.util.Color;

import java.nio.file.Path;

public class GuiGameSelectSP extends BorderPane {
    public GuiGameSelectSP(Path gameStorageBasePath) {
        setBackground(new Background(Color.fromRGB(0x666666)));
        padding().set(new Insets(5));
        var title = new Label("Play Singleplayer Game");
        title.setFont(new Font(title.getFont(), 24));
        var titleWrapper = new VBox();
        titleWrapper.alignment().set(HPos.CENTER);
        titleWrapper.getChildren().add(title);
        top().set(titleWrapper);

        var gameListView = new ListView<GameData>();
        gameListView.cellFactory().set(listView -> new ListCell<>() {
            @Override
            protected void updateItem(GameData item, boolean empty) {
                super.updateItem(item, empty);

                var wrapping = new VBox();
                var title = new Label("[PH] GameData");
                title.setFont(new Font(title.getFont(), 16));
                var desc = new Label("Nothing is here in this dimension...");
                desc.color().set(Color.fromGray(0.67f));
                wrapping.getChildren().addAll(title, desc);
                setGraphic(wrapping);
                if (!empty) {
                    title.text().set(item.getName());
                    desc.text().set(String.format("%d worlds, %d dependencies", item.getWorlds().size(), item.getDependencies().size()));
                }
            }
        });
        var storage = new GameDataStorage(gameStorageBasePath);
        gameListView.items().addAll(storage.getGames().values());
//        gameListView.getSize().setPrefSize(Size.USE_PARENT_VALUE,Size.USE_PARENT_VALUE);
        center().set(gameListView);

        var funcBox = new HBox();
        funcBox.alignment().set(VPos.CENTER);
        bottom().set(funcBox);
        var butPlay = new Button("Play");
        butPlay.setOnMouseClicked(event -> {
            var engine = Platform.getEngineClient();
            ((EngineClientImpl) engine).playIntegratedGame(gameListView.selectionModel().get().selectedItem().get().getName());
        });
        var butCreate = new Button("Create");
        butCreate.setOnMouseClicked(event -> {
            var guiManager = Platform.getEngineClient().getGraphicsManager().getGUIManager();
            guiManager.show(new Scene(new GuiCreateWorld(storage)));
        });
        var butDelete = new Button("Delete");
        butDelete.setOnMouseClicked(event -> {
            var popup = new Popup();
            VBox vBox = new VBox();
            vBox.alignment().set(HPos.CENTER);
            vBox.setBorder(new Border(Color.WHITE));
            Text text = new Text("Warning");
            Text info = new Text("Are you sure you want to delete this game?");

            var wrapping = new VBox();
            var lbl3 = new Label("[PH] GameData");
            lbl3.setFont(new Font(lbl3.getFont(), 16));
            var desc = new Label("Nothing is here in this dimension...");
            desc.color().set(Color.fromGray(0.67f));
            wrapping.getChildren().addAll(lbl3, desc);
            var data = gameListView.selectionModel().get().selectedItem().get();
            if (data != null) {
                lbl3.text().set(data.getName());
                desc.text().set(String.format("%d worlds, %d dependencies", data.getWorlds().size(), data.getDependencies().size()));
            }
            wrapping.border().set(new Border(Color.WHITE, 2));

            HBox func = new HBox();
            Button butConfirm = new Button("Confirm");
            Button butCancel = new Button("Cancel");
            func.getChildren().addAll(butConfirm, butCancel);
            butCancel.setOnAction(event1 -> popup.hide());
            vBox.getChildren().add(text);
            vBox.getChildren().add(info);
            vBox.getChildren().add(wrapping);
            vBox.getChildren().add(func);

            popup.setScene(new Scene(vBox));
            popup.setAutoFix(true);
            var stage = GuiGameSelectSP.this.getScene().getStage();
            popup.show(stage, stage.getX() + stage.getWidth() / 2f, stage.getY() + stage.getHeight() / 2f);
        });
        gameListView.selectionModel().get().selectedIndex().addChangeListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != -1) {
                butPlay.setDisabled(false);
                butDelete.setDisabled(false);
            } else {
                butPlay.setDisabled(true);
                butDelete.setDisabled(true);
            }
        });
        butPlay.setDisabled(true);
        butDelete.setDisabled(true);
        var butBack = new Button("Back");
        butBack.addEventHandler(MouseActionEvent.MOUSE_PRESSED, event -> {
            var guiManager = Platform.getEngineClient().getGraphicsManager().getGUIManager();
            guiManager.showLast();
        });
        funcBox.setSpacing(5);
        funcBox.getChildren().addAll(butPlay, butCreate, butDelete, butBack);
    }
}
