package org.wookie.carpetshopper;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.test.Mock;

@Mock
@RestClient
@ApplicationScoped
public class MockWeaverService implements WeaverService {

    @Override
    public Carpet weaveCarpet(CarpetOrder order) {
			return new Carpet(order.colour(), order.orderNumber());
    }
}

