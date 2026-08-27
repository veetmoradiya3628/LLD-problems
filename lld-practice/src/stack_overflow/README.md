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

#### 3. Design class & relationships

#### 4. Code Impl, Run & Test

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
                