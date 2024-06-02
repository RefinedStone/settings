package com.example.kotlinjdslexample

import com.example.kotlinjdslexample.entity.Book // ktlint-disable import-ordering
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
// import com.linecorp.kotlinjdsl.spring.data.reactive.query.updateQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.persistence.EntityManager
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/books/reactive")
class ReactiveBookController(
    private val bookService: ReactiveBookService,
) {
    @PostMapping
    suspend fun createBook(@RequestBody spec: CreateBookSpec): ResponseEntity<Long> {
        val book = bookService.create(spec)
        return ResponseEntity.ok(book.id)
    }

    @GetMapping("/all")
    suspend fun findByAll(): ResponseEntity<List<Book>> {
        val book = bookService.findAll()
        return ResponseEntity.ok(book)
    }
//    @GetMapping("/{bookId}")
//    suspend fun findById(@PathVariable bookId: Long): ResponseEntity<Book> {
//        val book = bookService.findById(bookId)
//
//        return ResponseEntity.ok(book)
//    }

//    @GetMapping("/{bookId}/toVO")
//    suspend fun findByIdToVO(@PathVariable bookId: Long): ResponseEntity<NameIsbnVO> {
//        val book = bookService.findByIdToVO(bookId)
//
//        return ResponseEntity.ok(book)
//    }
//
//    @GetMapping
//    suspend fun findAll(@RequestParam("name") name: String): ResponseEntity<List<Book>> {
//        val books = bookService.findAll(FindBookSpec(name = name))
//
//        return ResponseEntity.ok(books)
//    }
//
//    @GetMapping("/paging")
//    suspend fun findAllByPaging(
//        pageable: Pageable,
//        @RequestParam("name") name: String,
//        @RequestParam("publisher") publisher: String? = null,
//    ): ResponseEntity<Page<Book>> {
//        val books = bookService.findAll(FindBookSpec(name = name, publisher = publisher), pageable)
//
//        return ResponseEntity.ok(books)
//    }
//
//    @PutMapping
//    suspend fun update(@RequestBody spec: UpdateBookSpec): ResponseEntity<Int> {
//        val updatedRow = bookService.update(spec)
//
//        return ResponseEntity.ok(updatedRow)
//    }
//
//    @DeleteMapping
//    suspend fun delete(@RequestBody spec: FindBookSpec): ResponseEntity<Int> {
//        val updatedRow = bookService.delete(spec)
//
//        return ResponseEntity.ok(updatedRow)
}

interface BookRepository : JpaRepository<Book, Long>, KotlinJdslJpqlExecutor

@Service
class ReactiveBookService(
    private val sessionFactory: SessionFactory,
    private val entityManager: EntityManager,
    private val bookRepository: BookRepository,
) {
    private val context = JpqlRenderContext()
    suspend fun create(spec: CreateBookSpec): Book {
        return Book(name = spec.name, meta = spec.meta).also {
            sessionFactory.withSession { session -> session.persist(it).flatMap { session.flush() } }
                .awaitSuspending()
        }
    }

    suspend fun temp(): MutableList<Book>? {
        val query = jpql {
            select(entity(Book::class))
                .from(entity(Book::class))
        }
        val context = JpqlRenderContext()
        val renderer = JpqlRenderer()
        val rendered = renderer.render(query, context)

        val actual = sessionFactory.withSession { session ->
            session.createQuery(rendered.query, Book::class.java)
                .resultList
        }.awaitSuspending()
        return actual
    }

    suspend fun findAll(): List<Book> {
//        val rs: List<Book?> = bookReactiveRepository.findAll() {
//            select(entity(Book::class))
//                .from(entity(Book::class))
//        }
//        return rs.mapNotNull { it }
        val query = jpql {
            select(entity(Book::class))
                .from(entity(Book::class))
        }
        val context = JpqlRenderContext()
        val renderer = JpqlRenderer()
        val rendered = renderer.render(query, context)

        val actual = sessionFactory.withSession { session ->
            session.createQuery(rendered.query, Book::class.java)
                .resultList
        }.awaitSuspending()
        return actual
    }
//    suspend fun findById(id: Long): Book {
//        return queryFactory.singleQuery {
//            select(entity(Book::class))
//            from(entity(Book::class))
//            where(col(Book::id).equal(id))
//        }
//    }

//
//    suspend fun findByIdToVO(id: Long): NameIsbnVO {
//        return queryFactory.singleQuery {
//            selectMulti(col(Book::name), col(BookMeta::isbn10), col(BookMeta::isbn13))
//            from(entity(Book::class))
//            associate(Book::meta)
//            where(col(Book::id).equal(id))
//        }
//    }
//
//    suspend fun findAll(spec: FindBookSpec): List<Book> {
//        return queryFactory.listQuery {
//            select(entity(Book::class))
//            from(entity(Book::class))
//            where(findSpec(spec))
//        }
//    }
//
//    suspend fun update(spec: UpdateBookSpec): Int {
//        return queryFactory.updateQuery<Book> {
//            associate(Book::meta)
//            where(findSpec(spec.findBookSpec))
//            set(col(Book::name), spec.name)
//        }
//    }
//
//    suspend fun delete(spec: FindBookSpec): Int {
//        return queryFactory.deleteQuery<Book> {
//            associate(Book::meta)
//            where(findSpec(spec))
//        }
//    }
//
//    /**
//     * if you want reuse Predicates, extract Expressions and make an Extension like this method
//     * WhereDsl.xxxx
//     */
//    private fun WhereDsl.findSpec(spec: FindBookSpec) =
//        and(
//            col(Book::name).like("%${spec.name}%"),
//            spec.publisher?.let { col(BookMeta::publisher).equal(spec.publisher) },
//        )
//
//    suspend fun findAll(spec: FindBookSpec, pageable: Pageable): Page<Book> {
//        return queryFactory.pageQuery(pageable) {
//            select(entity(Book::class))
//            from(entity(Book::class))
//            where(findSpec(spec))
//        }
//    }
}
