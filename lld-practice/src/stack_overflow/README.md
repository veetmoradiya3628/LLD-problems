### Stack Overflow
Stack Overflow is one of the most widely used question-and-answer platforms for software developers. It enables users to ask technical programming questions, receive answers from the community, and collaboratively improve the quality of information through voting and editing.

#### 1. Requirement Gathering

- Functional Requirement
  - User can post questions, answers and comments on both questions and answers
  - Users can upvote or downvote questions and answers. A user can only vote once per post
  - The original poster of a question can accept one answer as the solution
  - A question can have one or more tags
  - Users earn or lose reputation points based on upvotes/downvotes on their content and whether their answer is accepted
  - Support searching for questions by keywords in the title or body and filtering questions by tags
- Non-Functional Requirement
  - Consistency: Voting actions and reputation updates should be strongly consistent and reflected immediately.
  - Concurrency: The system must gracefully handle high-concurrency scenarios, such as multiple users voting on the same post simultaneously.
  - Scalability: The design should be scalable to accommodate a growing number of users, questions, and answers.

#### 2. Core Identity
- User - class
  - Represents a registered user of the platform. Holds attributes like user ID, display name, and reputation. Responsible for posting content, voting, and earning points.
- Question
  - Represents a question posted by a user. Includes title, body, tags, creation timestamp, list of answers, list of comments, votes, and a reference to an accepted answer.
- Answer
  - Represents an answer posted to a question. Includes body, author, timestamp, list of comments, and votes. Can be marked as accepted by the original question author.
- Comment
  - Represents a comment made on a question or answer. Includes content, author, timestamp, and a reference to the parent post (either a question or answer).
- Vote
  - Represents a single upvote or downvote by a user on a question or answer. Includes voter, vote type (up or down), and the target post.
- Reputation
  - Represents a user’s score based on community interactions. May be modeled as a field in the User class or as a separate entity if detailed reputation history is needed.
- Tag
  - Represents a tag used to categorize questions. Each tag has a name.
- SearchService (Optional)
  - Provides methods to perform keyword search and tag-based filtering over the list of questions. May not be an entity per se but a key component of the system.

#### 3. Design class & relationships
- VoteType - ENUM
  - UPVOTE, DOWNVOTE
- EventType - ENUM
  - UPVOTE_QUESTION
  - DOWNVOTE_QUESTION
  - UPVOTE_ANSWER
  - DOWNVOTE_ANSWER
  - ACCEPT_ANSWER
- User - class
  - String id
  - String name
  - AtomicInteger reputation
  - +User(String name)
  - +updateReputation(int change): void
- Tag - class
  - String name
  - +Tag(String name)
- Content - abstract class
  - #String id
  - #String body
  - #User author
  - #LocalDateTime creationTime
  - +Content(String id, String body, User author)
- Post - abstract class extends Content
  - -int voteCount
  - -Map<String, VoteType> voters
  - -List<Comment> comments
  - -List<PostObserver> observers
  - +addObserver(PostObserver observer): void
  - #nofifyObservers(Event event): void
  - +addComment(Comment comment): void
  - +vote(User user, VoteType voteType): void
- Question - class
  - String title
  - Set<Tag> tags
  - List<Answer> answers
  - Answer acceptedAnswer
  - +addAnswer(Answer answer): void
  - +acceptAnswer(Answer answer): void
- Answer - class
  - boolean isAccepted
- Comment - class
  - +Comment(String body, User author)
- StackOverflowService - class
  - Map<String, User> users
  - Map<String, Question> questions
  - Map<String, Answer> answers
  - PostObserver reputationManager
  - createUser(String name): User
  - postQuestion(String userId, String title, String body, Set<Tag> tags): Question
  - postAnswer(String userId, String questionId, String body): Answer
  - addComment(String userid, String postId, String body): Comment
  - voteOnPost(String postId, String userId, VoteType vote): void
  - acceptAnswer(String questionId, String answerId): void
  - searchQuestions(List<SearchStrategy> strategies): List<Question>
  - getUser(String userId): User
  - findPostById(String postId): Post
- Observer pattern for vote and reputation management
- Strategy pattern for Searching the question
- Facade pattern for StackOverflowService which provides high level methods for stack overflow actions

#### 4. Code Impl, Run & Test
- Clean code implemented

#### 5. Concurrency & Thread Safety
1. Two Users Voting on the Same Post at Once
- `vote` method is marked as synchronized

2. Reputation Drifting Under Concurrent Votes
- storing `reputation` as `AtomicInteger`

3. Accepting an Answer While It Is Being Voted On
- marked `acceptAnswer` as synchronized

#### 6. Extensions
1. Badges and Gamification 
- Award badges when a user crosses milestones, such as a first accepted answer or ten upvotes on a single answer.
- We can introduce BadgeManager to delegate this operations

2. Full-Text Search Over Questions
- The keyword search scans every question body on each query, which gets slow as the question count grows. Speed it up with an index.
- Introduce `IndexedSearchStrategy` builds an inverted index from each term to the questions that contain it, then answers a keyword query from the index instead of scanning every body.

3. Edit History and Version Control of Posts
- Posts can be edited. Keep every previous version so users can see the edit history and revert if needed.
- Introduce something like `PostRevision` to manage this.

#### Design patterns & Principles
- Manager / Facade - StackOverflow service class
- Strategy - for question search implementation
- Observer - for post action and its implementation
- Builder / Factory can be utilized for more robust and clean code 

#### Open issues
TODO
                