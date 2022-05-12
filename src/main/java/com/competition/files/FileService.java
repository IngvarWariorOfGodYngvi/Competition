package com.competition.files;

import com.competition.club.ClubRepository;
import com.competition.player.Player;
import com.competition.player.PlayerRepository;
import com.competition.player.PlayerService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final ClubRepository clubRepository;

    private final FileRepository fileRepository;

    private final PlayerService playerService;

    private final PlayerRepository playerRepository;

    public FileService(ClubRepository clubRepository, FileRepository fileRepository, PlayerService playerService, PlayerRepository playerRepository) {
        this.clubRepository = clubRepository;
        this.fileRepository = fileRepository;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
    }

    public File printAll() throws IOException, DocumentException {
        String fileName = "Lista_startowa.pdf";
        Document document = new Document(PageSize.A4.rotate());

        List<Player> all = playerRepository.findAll().stream().sorted(Comparator.comparing(Player::getStartNumber)).collect(Collectors.toList());

        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(fileName));
        document.open();
        document.addTitle(fileName);
        document.addCreationDate();
        for (Player player : all) {
            int size = 40;
            int bold = 1;
            Paragraph space = new Paragraph(" ", font(100, bold));
            Paragraph number = new Paragraph(player.getStartNumber(), font(size+30, bold));
            Paragraph name = new Paragraph(player.getSecondName().toUpperCase() + " " + player.getFirstName(), font(size, bold));
            Paragraph club = new Paragraph(player.getClub().getName().toUpperCase() + " " + player.getClub().getCity(), font(size, bold));
            //0             1               2
            int align = Element.ALIGN_CENTER;
            space.setAlignment(align);
            number.setAlignment(align);
            name.setAlignment(align);
            club.setAlignment(align);

            document.add(space);
            document.add(number);
            document.add(name);
            document.add(club);
            document.newPage();

        }
        document.close();


        byte[] data = convertToByteArray(fileName);
        File fileM = File.builder()
                .name(fileName)
                .data(data)
                .type(String.valueOf(MediaType.APPLICATION_PDF))
                .size(data.length)
                .build();

        File fileE =
                createFileEntity(fileM);

        java.io.File file = new java.io.File(fileName);

        file.delete();
        return fileE;
    }

    //      read file
    public ResponseEntity<?> importDataFromCSV(String path) {

        Path pathToFile = Paths.get(path);
        List<String> list = new ArrayList<>();
        try {
            BufferedReader br = Files.newBufferedReader(pathToFile, Charset.defaultCharset());
            String line = br.readLine();
            while (line !=null) {
                String[] data = line.split(";");
                System.out.println(Arrays.asList(data));
                String player = playerService.createPlayer(data);
                list.add(player);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        List<Player> all = playerRepository.findAll();

        List<Player> sorted = all.stream()
                .filter(f -> f.getClub() != null)
                .sorted(Comparator.comparing(o -> o.getClub().getName()))
                .collect(Collectors.toList());

        AtomicInteger i = new AtomicInteger();
        i.getAndAdd(1);
        String number = "";

        sorted.forEach(e -> {
            String number1 = "";

            if (i.intValue() > 0 && i.intValue() < 10) {
                number1 = "00" + i;
            } else if (i.intValue() > 10 && i.intValue() < 100) {
                number1 = "0" + i;
            } else {
                number1 = String.valueOf(i.intValue());
            }
            e.setStartNumber(number1);
            i.getAndIncrement();
            playerRepository.save(e);
        });

        return ResponseEntity.ok(list);
    }

    public String store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        File build = File.builder()
                .name(fileName)
                .type(String.valueOf(file.getContentType()))
                .data(file.getBytes())
                .size(file.getSize())
                .build();
        File fileEntity = createFileEntity(build);

        return fileEntity.getUuid();
    }

    File createFileEntity(File file) {
        file.setDate(LocalDate.now());
        file.setTime(LocalTime.now());
        return fileRepository.save(file);

    }

    public File generateCSV(int numberOfRecords) throws IOException {
        String fileName = "generowane.txt";
        FileOutputStream outputStream = new FileOutputStream(fileName);
        for (int i = 0; i < numberOfRecords; i++) {
            String line = "Kowalski" + i + ";" + "Jan" + i
                    + ";" + LocalDate.now().getYear()
                    + ";L-" + new Random().nextInt(99999)
                    + ";" + "Klub" + i % 10
                    + ";" + "Miasto" + i % 10 + "\n";

            byte[] strToBytes = line.getBytes();
            outputStream.write(strToBytes);
        }
        outputStream.close();

        byte[] data = convertToByteArray(fileName);
        File fileM = File.builder()
                .name(fileName)
                .data(data)
                .type(String.valueOf(MediaType.APPLICATION_PDF))
                .size(data.length)
                .build();

        File fileE =
                createFileEntity(fileM);

        java.io.File file = new java.io.File(fileName);

        file.delete();
        return fileE;
    }

    public File getFile(String uuid) {
        return fileRepository.getOne(uuid);
    }

    private byte[] convertToByteArray(String path) throws IOException {
        java.io.File file = new java.io.File(path);
        return Files.readAllBytes(file.toPath());

    }

    /**
     * 1 - BOLD , 2 - ITALIC, 3 - BOLDITALIC
     *
     * @param size  set font size
     * @param style set style Bold/Italic/Bolditalic
     * @return returns new font
     */
    private Font font(int size, int style) throws IOException, DocumentException {
        BaseFont czcionka = BaseFont.createFont("font/times.ttf", BaseFont.IDENTITY_H, BaseFont.CACHED);
        return new Font(czcionka, size, style);
    }
}
