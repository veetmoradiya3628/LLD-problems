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

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
                