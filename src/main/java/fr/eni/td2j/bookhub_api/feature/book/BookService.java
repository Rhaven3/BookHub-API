package fr.eni.td2j.bookhub_api.feature.book;

import fr.eni.td2j.bookhub_api.exception.BadRequestException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.author.Author;
import fr.eni.td2j.bookhub_api.feature.author.AuthorRepository;
import fr.eni.td2j.bookhub_api.feature.author.AuthorService;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookRequestDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.BookResponseDTO;
import fr.eni.td2j.bookhub_api.feature.book.dto.mapper.BookMapper;
import fr.eni.td2j.bookhub_api.feature.category.Category;
import fr.eni.td2j.bookhub_api.feature.category.CategoryRepository;
import fr.eni.td2j.bookhub_api.feature.editor.Editor;
import fr.eni.td2j.bookhub_api.feature.editor.EditorRepository;
import fr.eni.td2j.bookhub_api.feature.image.Image;
import fr.eni.td2j.bookhub_api.feature.image.ImageRepository;
import fr.eni.td2j.bookhub_api.feature.image.dto.ImageRequestDTO;
import fr.eni.td2j.bookhub_api.feature.image.services.ImageService;
import fr.eni.td2j.bookhub_api.feature.image.services.storage.IFileStorageService;
import fr.eni.td2j.bookhub_api.feature.loan.Loan;
import fr.eni.td2j.bookhub_api.feature.loan.LoanEnum;
import fr.eni.td2j.bookhub_api.feature.loan.LoanRepository;
import fr.eni.td2j.bookhub_api.feature.reservation.Reservation;
import fr.eni.td2j.bookhub_api.feature.reservation.ReservationRepository;
import fr.eni.td2j.bookhub_api.feature.reservation.emuns.ReservationEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final EditorRepository editorRepository;
    private final ReservationRepository reservationRepository;
    private final LoanRepository loanRepository;
    private final ImageRepository imageRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final IFileStorageService fileStorageService;
    private final ImageService imageService;

    public Page<BookResponseDTO> findAll(Pageable pageable) {
         Page<Book> books = bookRepository.findAll(pageable);
         return books.map(bookMapper::toDto);
    }

    public BookResponseDTO findById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    @Transactional
    public BookResponseDTO create(BookRequestDTO dto, List<MultipartFile> files) {
        List<Author> authors = new ArrayList<>();
        authors.addAll(getAuthors(dto.getAuthorIds()));
        authors.addAll(authorService.createAuthors(dto.getNewAuthors()));

        if (authors.isEmpty()) {
            throw new BadRequestException("Le livre doit avoir au moins un auteur.");
        }

        List<Category> categories = getCategories(dto.getCategoryIds());
        List<Image> images = createImages(dto.getNewImageNames(), files);

        Editor editor = getEditor(dto.getEditorId());

        Book book = Book.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .publishDate(dto.getPublishDate())
                .language(dto.getLanguage())
                .isbn(dto.getIsbn())
                .available(true)
                .owned(true)
                .editor(editor)
                .authors(authors)
                .categories(categories)
                .images(images)
                .build();

        return bookMapper.toDto(bookRepository.save(book));
    }

    private List<Image> createImages(List<String> imageNames, List<MultipartFile> files) {
        if (imageNames == null || imageNames.isEmpty()) return List.of();

        if (files == null || files.size() != imageNames.size()) {
            throw new BadRequestException("Le nombre de fichiers ne correspond pas au nombre de noms fournis.");
        }

        List<Image> result = new ArrayList<>();
        for (int i = 0; i < imageNames.size(); i++) {
            String name = imageNames.get(i);
            imageService.isExisting(name); // lève déjà BadRequestException si nom pris

            String path = fileStorageService.store(files.get(i));
            result.add(imageService.addImage(Image.builder().name(name).path(path).build()));
        }
        return result;
    }

    private List<Category> getCategories(List<Long> categoryIds) {

        if(categoryIds == null || categoryIds.isEmpty()){
            return List.of();
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        if(categories.size() != categoryIds.size()){
            throw new NotFoundException("Une ou plusieurs catégories sont introuvables.");
        }

        return categories;
    }

    private List<Image> getImages(List<Long> imageIds) {

        if(imageIds == null || imageIds.isEmpty()){
            return List.of();
        }

        List<Image> images = imageRepository.findAllById(imageIds);

        if (images.size() != imageIds.size()) {
            throw new NotFoundException("Une ou plusieurs images sont introuvables.");
        }
        return images;
    }

    private List<Author> getAuthors(List<Long> authorIds) {

        if(authorIds == null || authorIds.isEmpty()){
            return List.of();
        }

        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            throw new NotFoundException("Un ou plusieurs auteurs sont introuvables.");
        }
        return authors;
    }

    private Editor getEditor(Long editorId) {
        return editorRepository.findById(editorId)
                .orElseThrow(() -> new NotFoundException("Éditeur introuvable."));
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO requestBook, List<MultipartFile> files) {

        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (requestBook.getTitle() != null) {
            existingBook.setTitle(requestBook.getTitle());
        }

        if (requestBook.getDescription() != null) {
            existingBook.setDescription(requestBook.getDescription());
        }

        if (requestBook.getPublishDate() != null) {
            existingBook.setPublishDate(requestBook.getPublishDate());
        }

        if (requestBook.getLanguage() != null) {
            existingBook.setLanguage(requestBook.getLanguage());
        }

        if (requestBook.getIsbn() != null) {
            existingBook.setIsbn(requestBook.getIsbn());
        }

        if (requestBook.getEditorId() != null) {
            existingBook.setEditor(getEditor(requestBook.getEditorId()));
        }

        if (requestBook.getAuthorIds() != null || requestBook.getNewAuthors() != null) {
            List<Author> authors = new ArrayList<>();

            if (requestBook.getAuthorIds() != null) {
                authors.addAll(getAuthors(requestBook.getAuthorIds()));
            }
            if (requestBook.getNewAuthors() != null) {
                authors.addAll(authorService.createAuthors(requestBook.getNewAuthors()));
            }

            if (authors.isEmpty()) {
                throw new BadRequestException("Le livre doit avoir au moins un auteur.");
            }

            existingBook.setAuthors(authors);
        }

        if (requestBook.getCategoryIds() != null) {
            existingBook.setCategories(getCategories(requestBook.getCategoryIds()));
        }

        // Images : remplacement complet — les images existantes conservées
        // (keepImageIds) + les nouvelles créées à partir des fichiers reçus
        if (requestBook.getKeepImageIds() != null || requestBook.getNewImageNames() != null) {
            List<Image> keptImages = requestBook.getKeepImageIds() != null
                    ? getImages(requestBook.getKeepImageIds())
                    : List.of();

            List<Image> newImages = createImages(requestBook.getNewImageNames(), files);

            List<Image> allImages = new ArrayList<>();
            allImages.addAll(keptImages);
            allImages.addAll(newImages);

            existingBook.getImages().clear();
            existingBook.getImages().addAll(allImages);
        }

        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    public void delete(Long id) {

        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Livre introuvable."));

        if (!book.isAvailable()) {
            throw new BadRequestException("Impossible de supprimer un livre actuellement emprunté.");
        }

        if(!book.isOwned()){
            throw new BadRequestException("Impossible de supprimer un livre externe.");
        }

        bookRepository.delete(book);
    }

    public BookResponseDTO findByTitle(String title) {

        if(title == null || title.isBlank()){
            throw new BadRequestException("Le titre du livre est obligatoire.");
        }

        Book book = bookRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public BookResponseDTO findByIsbn(String isbn) {

        if(isbn == null || isbn.isBlank()){
            throw new BadRequestException("L'ISBN du livre est obligatoire.");
        }

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException("Livre introuvable."));

        return bookMapper.toDto(book);
    }

    public Page<BookResponseDTO> findByAvailable(boolean available, Pageable pageable) {

        Page<Book> books = bookRepository.findByAvailable(available, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException(
                    "Aucun livre trouvé avec ce statut de disponibilité."
            );
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByAuthor(Long authorId, Pageable pageable) {

        Page<Book> books = bookRepository.findByAuthorsId(authorId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cet auteur.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByCategory(Long categoryId, Pageable pageable) {

        Page<Book> books = bookRepository.findByCategoriesId(categoryId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cette catégorie.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> findByEditor(Long editorId, Pageable pageable) {

        Page<Book> books = bookRepository.findByEditorId(editorId, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException("Aucun livre trouvé pour cet éditeur.");
        }

        return books.map(bookMapper::toDto);
    }

    public Page<BookResponseDTO> search(String word, Pageable pageable) {

        if (word == null || word.isBlank()) {
            throw new BadRequestException(
                    "Le mot de recherche est obligatoire."
            );
        }

        Page<Book> books = bookRepository.search(word, pageable);

        if (books.isEmpty()) {
            throw new NotFoundException(
                    "Aucun livre trouvé pour cette recherche."
            );
        }

        return books.map(bookMapper::toDto);
    }

    public boolean isBookAvailable(Book book) {
        List<Reservation> reservations = reservationRepository.findByBookAndStatus(book, ReservationEnum.WAITING);
        reservations.addAll(reservationRepository.findByBookAndStatus(book, ReservationEnum.AVAILABLE));
        if (!reservations.isEmpty()) {
            return false;
        }
        List<Loan> loans = loanRepository.findByBookAndStatus(book, LoanEnum.IN_PROGRESS);
        if (!loans.isEmpty()) {
            return false;
        }
        return true;
    }

    public Page<BookResponseDTO> filter(Long authorId, Long categoryId, Long editorId, Pageable pageable) {

        Page<Book> books = bookRepository.filter(authorId, categoryId, editorId, pageable);
        return books.map(bookMapper::toDto);

    }

}
