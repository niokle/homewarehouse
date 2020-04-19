package com.klenio.homewarehouse.views.list;

import com.klenio.homewarehouse.backend.MyBackendService;
import com.klenio.homewarehouse.backend.MyExternalData;
import com.klenio.homewarehouse.backend.MyProduct;
import com.klenio.homewarehouse.views.main.MyMainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "list", layout = MyMainView.class)
@RouteAlias(value = "", layout = MyMainView.class)
@PageTitle("Home Warehouse")
@CssImport("styles/views/list/list-view.css")
public class MyListView extends Div implements AfterNavigationObserver {
    @Autowired
    private MyBackendService myBackendService;

    @Autowired
    private MyExternalData myExternalData;

    private Grid<MyProduct> myProductGrid;

    private TextField date = new TextField();
    private TextField name = new TextField();
    private TextField place = new TextField();
    private TextField status = new TextField();

    private TextField search = new TextField();

    private Button buttonInUse = new Button("W użyciu");
    private Button buttonEaten = new Button("Zjedzone");
    private Button buttonClear = new Button("Wyczyść");
    private Button buttonAdd = new Button("Dodaj");

    private Binder<MyProduct> binder;

    private MyProduct activeMyProduct;

    private Notification notification;

    public MyListView() {
        setId("list-view");
        // Configure Grid
        myProductGrid = new Grid<>();
        myProductGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        myProductGrid.setHeightFull();
        myProductGrid.addColumn(MyProduct::getDate).setHeader("Data").setAutoWidth(true);
        myProductGrid.addColumn(MyProduct::getName).setHeader("Produkt").setAutoWidth(true);
        myProductGrid.addColumn(MyProduct::getPlace).setHeader("Miejsce").setAutoWidth(true);
        myProductGrid.addColumn(MyProduct::getStatus).setHeader("Status").setAutoWidth(true);

        //when a row is selected or deselected, populate form
        myProductGrid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(MyProduct.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
        // note that password field isn't bound since that property doesn't exist in
        // Employee

        // the grid valueChangeEvent will clear the form too
        buttonClear.addClickListener(e -> myProductGrid.asSingleSelect().clear());

        buttonInUse.addClickListener(e -> {
            if (isActiveMyProduct()) {
                this.status.setValue("w użyciu");
                this.buttonAdd.click();
                this.buttonClear.click();
                notificationShow("Zmieniono status na 'w użyciu'", NotificationVariant.LUMO_SUCCESS);
            } else {
                notificationShow("Nie wybrano produktu", NotificationVariant.LUMO_ERROR);
            }
        });

        buttonEaten.addClickListener(e -> {
            if (isActiveMyProduct()) {
                this.status.setValue("zjedzone");
                this.buttonAdd.click();
                this.buttonClear.click();
                notificationShow("Zmieniono status na 'zjedzone'", NotificationVariant.LUMO_SUCCESS);
            } else {
                notificationShow("Nie wybrano produktu", NotificationVariant.LUMO_ERROR);
            }
        });

        buttonAdd.addClickListener(e -> {
            if (saveData()) {
                notificationShow("Zapisano", NotificationVariant.LUMO_SUCCESS);
                loadFullOrFilteredList();
            }
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    private void notificationShow(String text, NotificationVariant notificationVariant) {
        notification = new Notification();
        notification.addThemeVariants(notificationVariant);
        notification.add(text);
        notification.setDuration(1000);
        notification.open();
    }

    private boolean isActiveMyProduct() {
        return activeMyProduct != null;
    }

    private boolean saveData() {
        if (activeMyProduct != null) {
            activeMyProduct.setDate(date.getValue());
            activeMyProduct.setName(name.getValue());
            activeMyProduct.setPlace(place.getValue());
            activeMyProduct.setStatus(status.getValue());
            activeMyProduct = null;
            myBackendService.setMyProducts(myExternalData);
            return true;
        } else {
            if (isCorrect()) {
                Long newId = myBackendService.getMyProducts(myExternalData).stream().max((prod1, prod2) -> Long.compare(prod1.getId(), prod2.getId())).get().getId() + 1;
                myBackendService.addMyProduct(new MyProduct(newId, date.getValue(), name.getValue(), place.getValue(), status.getValue()));
                myBackendService.setMyProducts(myExternalData);
                buttonClear.click();
                return true;
            } else {
                notificationShow("Nie zapisano! Uzupełnij wszystkie pola!", NotificationVariant.LUMO_ERROR);
                return false;
            }
        }
    }

    private boolean isCorrect() {
        return  date.getValue().length() > 0 && name.getValue().length() > 0 &&
                place.getValue().length() > 0;
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorDiv = new Div();
        searchLayout(editorDiv);
        createButtonLayout(editorDiv);
        editorDiv.setId("editor-layout");
        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, date, "Data");
        addFormItem(editorDiv, formLayout, name, "Produkt");
        addFormItem(editorDiv, formLayout, place, "Miejsce");
        addFormItem(editorDiv, formLayout, status, "Status");
        splitLayout.addToSecondary(editorDiv);
        splitLayout.setSplitterPosition(80);
    }

    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        //VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setId("button-layout");
        //buttonLayout.setWidthFull();
        buttonLayout.setHeight("");
        buttonLayout.setSpacing(true);
        buttonInUse.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        buttonEaten.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonClear.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        buttonLayout.add(buttonAdd, buttonInUse, buttonEaten, buttonClear);
        editorDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(myProductGrid);
    }

    private void addFormItem(Div wrapper, FormLayout formLayout,
            AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
        loadFullOrFilteredList();
        //todo
        test();
    }

    private void populateForm(MyProduct value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
        activeMyProduct = value;
    }

    //todo
    private void test() {
        //com.vaadin.flow.component.grid.
    }

    private void searchLayout(Div editorDiv) {
        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, search, "Szukaj");
        search.addValueChangeListener(e -> loadFilteredList());
        search.setClearButtonVisible(true);
    }

    private void loadFilteredList() {
        myProductGrid.setItems(myBackendService.getMyFilteredProducts(myExternalData, search.getValue()));
    }

    private void loadFullList() {
        myProductGrid.setItems(myBackendService.getMyProducts(myExternalData));
    }

    private void loadFullOrFilteredList() {
        if (search.getValue().length() > 0) {
            loadFilteredList();
        } else {
            loadFullList();
        }
    }
}
