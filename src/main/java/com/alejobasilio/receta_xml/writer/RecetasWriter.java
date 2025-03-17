package com.alejobasilio.receta_xml.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import com.alejobasilio.receta_xml.model.Receta;

@Component
public class RecetasWriter implements ItemWriter<Receta>, ItemStream  {

    private StaxEventItemWriter<Receta> writer;

    public RecetasWriter() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.alejobasilio.receta_xml.model");

        writer = new StaxEventItemWriterBuilder<Receta>()
                .name("recetasWriter")
                .marshaller(marshaller)
                .resource(new FileSystemResource("src/main/resources/output/recetas.xml"))
                .rootTagName("receta")
                .overwriteOutput(true)
                .build();
    }

    @Override
    public void write(Chunk<? extends Receta> chunk) throws Exception {
    	
        writer.write(chunk);
    }
  
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        writer.open(executionContext);
    }
    
    @Override
    public void close() throws ItemStreamException {
        writer.close();
    }
}
