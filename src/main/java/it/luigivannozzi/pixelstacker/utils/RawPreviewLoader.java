package it.luigivannozzi.pixelstacker.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifThumbnailDirectory;
import it.luigivannozzi.pixelstacker.exceptions.RawImageLoadException;
import it.luigivannozzi.pixelstacker.exceptions.ReadSegmentAsImageException;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

// loader per anteprime RAW (Java non può leggere file RAW dunque viene utilizzato il JPEG incorporato)
// utilizza metodi static e non deve essere istanziata
public final class RawPreviewLoader {

    private RawPreviewLoader() {}

    // metodo statico che prova a leggere i metadati presenti nei file RAW per individuare la presenza di una
    // miniatura JPEG (normalmente inclusa) e recuperare tale miniatura per visualizzarla nelle preview
    public static Image load(Path rawPath) {
        try {
            // legge i metadati dal file puntato da rawPath
            Metadata meta = ImageMetadataReader.readMetadata(rawPath.toFile());

            // Cerca la directory IFD1, usata in molti RAW come area dedicata alle miniature
            ExifThumbnailDirectory ifd1 = meta.getFirstDirectoryOfType(ExifThumbnailDirectory.class);
            if (ifd1 != null) {
                // se IFD1 esiste, legge due tag standard:
                // TAG_THUMBNAIL_OFFSET - offset (in byte) della miniatura nel file
                // TAG_THUMBNAIL_LENGTH - lunghezza (in byte) della miniatura
                Long off = ifd1.getLongObject(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET);
                Long len = ifd1.getLongObject(ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH);
                // estrae il segmento e lo decodifica come immagine attraverso il metodo readSegmentAsImage
                Image img = readSegmentAsImage(rawPath, off, len);
                if (img != null) return img;
            }
        } catch (IOException e) {
            throw new RawImageLoadException("Errore di I/O su file RAW: " + rawPath);
        } catch (ImageProcessingException e) {
            throw new RawImageLoadException("Impossibile leggere i metadati EXIF da: " + rawPath);
        } catch (Exception e) {
            throw new RawImageLoadException("Errore sconosciuto durante il caricamento di " + rawPath);
        }

        System.out.println("Nessuna preview trovata per l'immagine " + rawPath);
        return null;
    }

    // metodo che legge un segmento del file e prova a decodificarlo come immagine
    private static Image readSegmentAsImage(Path rawPath, Long offset, Long length) {
        try {
            if (offset != null && length != null && offset >= 0 && length > 0) {
                // apre un RandomAccessFile in modalità read-only (r) e lo chiude automaticamente a fine blocco
                try (RandomAccessFile raf = new RandomAccessFile(rawPath.toFile(), "r")) {
                    // controllo sulla lunghezza massima
                    // in Java un array richiede una dimensione massima pari al massimo valore assegnabile a un int
                    if (length <= Integer.MAX_VALUE) {
                        byte[] bytes = new byte[length.intValue()];
                        // posiziona il puntatore di lettura all’offset richiesto
                        raf.seek(offset);
                        // legge esattamente length byte nel buffer
                        raf.readFully(bytes);
                        // converte l'array di bytes in un InputStream senza ridimensionare (0)
                        return new Image(new ByteArrayInputStream(bytes),
                                0, 0, true, true);
                    } else {
                        throw new ReadSegmentAsImageException(
                                "Dimensione troppo grande per allocare l'array: " + length + " bytes");
                    }
                }
            }
        } catch (Exception e) {
            throw new ReadSegmentAsImageException("Errore durante la lettura dei bytes dai metadati: " + rawPath);
        }
        return null;
    }
}