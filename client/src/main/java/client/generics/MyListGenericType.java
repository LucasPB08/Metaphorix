package client.generics;

import jakarta.ws.rs.core.GenericType;

import java.util.List;

public class MyListGenericType<T> extends GenericType<List<T>> {
}
