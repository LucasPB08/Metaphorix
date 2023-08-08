package api.fakes;

import commons.GroupChatDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import commons.ChatUser;
import server.database.ChatUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ChatUserRepoFake implements ChatUserRepository {
    private List<ChatUser> users = new ArrayList<>();
    private String lastCall = "";
    private List<String> calls = new ArrayList<>();

    public List<ChatUser> getUsers(){
        return users;
    }

    public void users(List<ChatUser> users){
        this.users= users;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends ChatUser> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ChatUser> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<ChatUser> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ChatUser getOne(String s) {
        return null;
    }

    @Override
    public ChatUser getById(String s) {
        return null;
    }

    @Override
    public ChatUser getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends ChatUser> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ChatUser> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends ChatUser> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends ChatUser> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ChatUser> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ChatUser> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ChatUser, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ChatUser> S save(S entity) {
        users.add(entity);
        lastCall = "save";
        calls.add("save");
        return entity;
    }

    @Override
    public <S extends ChatUser> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ChatUser> findById(String s) {
        ChatUser user = null;

        for(ChatUser u: users){
            if(u.getUserName().equals(s))
                user = u;
        }



        return user == null ? Optional.empty():Optional.of(user);
    }

    @Override
    public boolean existsById(String s) {
        lastCall = "existsById";
        calls.add("existsById");

        for(ChatUser user: users){
            if(user.getUserName().equals(s))
                return true;
        }

        return false;
    }

    @Override
    public List<ChatUser> findAll() {
        return users;
    }

    @Override
    public List<ChatUser> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(ChatUser entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ChatUser> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ChatUser> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ChatUser> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<GroupChatDTO> findAllGroupChats(String userId) {
        return null;
    }
}
