package projects.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import projects.dao.BookDao;
import projects.dao.PersonDao;
import projects.models.Book;
import projects.models.Person;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private  BookDao bookDao;
    private PersonDao personDao;

    @Autowired
    public BookController(BookDao bookDao, PersonDao personDao) {
        this.bookDao = bookDao;
        this.personDao = personDao;
    }

    @GetMapping()
    public String books(Model model) {
        model.addAttribute("books",bookDao.getAllBooks());
        return "/book/all";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book",bookDao.getBookById(id));

        Optional<Person> bookOwner=bookDao.getBookOwner(id);

        if(bookOwner.isPresent()) {
            model.addAttribute("owner",bookOwner.get());
        }else
            model.addAttribute("people",personDao.getAll());

        return "/book/showBook";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/book/newBook";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/book/newBook";
        }

        bookDao.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("book",bookDao.getBookById(id));
        return "/book/editBook";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/book/editBook";
        }

        bookDao.update(book,id);
        return "redirect:/people";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookDao.release(id);
        return "redirect:/books/"+id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person")Person selectedPerson) {
        bookDao.assign(id,selectedPerson);
        return "redirect:/books/"+id;
    }

}
