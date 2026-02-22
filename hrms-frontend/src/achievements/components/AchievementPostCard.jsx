import React, { useMemo, useState } from "react";
import MediaCarousel from "./MediaCarousel";
import { toggleAchievementLike, addComment as apiAddComment } from "../../achievements/achievementsAPI";
import { useAuth } from "../../context/AuthContext";

const relativeTime = (iso) => {
  if (!iso) return "";
  const then = new Date(iso);
  const diff = Math.floor((Date.now() - then.getTime()) / 1000);
  if (diff < 60) return `${diff}s ago`;
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
  return `${Math.floor(diff / 86400)}d ago`;
};

const bytesToReadable = (bytes) => {
  if (!bytes) return "0 MB";
  const mb = bytes / 1024 / 1024;
  if (mb < 1024) return `${mb.toFixed(2)} MB`;
  return `${(mb / 1024).toFixed(2)} GB`;
};

const Tag = ({ tag, onClick }) => (
  <button onClick={() => onClick && onClick(tag)} className="px-2 py-1 bg-gray-100 hover:bg-gray-200 text-xs rounded mr-2">#{tag}</button>
);

const AchievementPostCard = ({
  post,
  onLike,
  onUnlike,
  onAddComment,
  onDeleteComment,
  onEditComment,
  onEditPost,
  onDeletePost,
  onTagClick,
  onAuthorClick,
  currentUserId,
  userRole,
  className = "",
}) => {
  const { user } = useAuth();
  const [liked, setLiked] = useState(Boolean(post.isLikedByCurrentUser));
  const [likeCount, setLikeCount] = useState(Number(post.likeCount || 0));
  const [commentsOpen, setCommentsOpen] = useState(false);
  const [commentText, setCommentText] = useState("");
  const [submittingComment, setSubmittingComment] = useState(false);

  const mediaList = useMemo(() => post.media || [], [post.media]);

  const handleToggleLike = async () => {
    // optimistic
    const nextLiked = !liked;
    setLiked(nextLiked);
    setLikeCount((c) => (nextLiked ? c + 1 : Math.max(0, c - 1)));
    try {
      await toggleAchievementLike(post.postId);
      if (nextLiked) onLike && onLike(post.postId);
      else onUnlike && onUnlike(post.postId);
    } catch (err) {
      // revert
      setLiked((v) => !v);
      setLikeCount((c) => (nextLiked ? Math.max(0, c - 1) : c + 1));
      console.error("Like failed", err);
    }
  };

  const visibleComments = useMemo(() => {
    const list = Array.isArray(post.comments) ? post.comments : post.comments?.content || [];
    // newest first
    return [...list].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  }, [post.comments]);

  const handleSubmitComment = async () => {
    if (!commentText.trim()) return;
    setSubmittingComment(true);
    try {
      const created = await apiAddComment(post.postId, commentText.trim());
      // update local list if parent didn't handle
      if (onAddComment) onAddComment(post.postId, commentText.trim());
      setCommentText("");
      setCommentsOpen(true);
    } catch (err) {
      console.error("Add comment failed", err);
    } finally {
      setSubmittingComment(false);
    }
  };

  const isAuthor = user && (user.id === post.author?.id || user.id === currentUserId);
  const canModerate = userRole === "HR" || user?.role === "HR";

  return (
    <article className={`bg-white border border-gray-200 rounded-lg shadow-sm p-4 mb-4 ${className}`} role="article" aria-label={`Post by ${post.author?.name}`}>
      {/* Header */}
      <header className="flex items-start gap-3">
        <button onClick={() => onAuthorClick && onAuthorClick(post.author?.id)} aria-label="Open author profile">
          <img src={post.author?.profileImage || "/uploads/default-avatar.png"} alt={post.author?.name || "Author"} className="w-10 h-10 rounded-full object-cover" />
        </button>
        <div className="flex-1">
          <div className="flex items-center gap-2">
            <button onClick={() => onAuthorClick && onAuthorClick(post.author?.id)} className="text-sm font-semibold text-gray-900 hover:underline">{post.author?.name}</button>
            <span className="text-xs text-gray-400">{post.author?.designation}</span>
            <span className="text-xs text-gray-400">•</span>
            <span className="text-xs text-gray-400">{relativeTime(post.createdAt)}</span>
            <span className="ml-2 px-2 py-0.5 text-xs bg-gray-100 rounded">{post.visibility}</span>
            {post.isSystemGenerated && <span className="ml-2 px-2 py-0.5 text-xs bg-yellow-100 text-yellow-800 rounded">System Post</span>}
          </div>
        </div>

        <div>
          {/* More options */}
          {(isAuthor || canModerate) && (
            <div className="text-gray-500">⋮</div>
          )}
        </div>
      </header>

      {/* Content */}
      <div className="mt-3">
        {post.title && <h3 className="text-[18px] font-bold text-[#333]">{post.title}</h3>}
        <p className="mt-2 text-sm text-gray-700 whitespace-pre-line">{post.description}</p>

        {/* Tags */}
        {post.tags && (
          <div className="mt-3 flex flex-wrap">
            {post.tags.split(",").map((t) => t.trim()).filter(Boolean).map((tag) => (
              <Tag key={tag} tag={tag} onClick={onTagClick} />
            ))}
          </div>
        )}
      </div>

      {/* Media */}
      <div className="mt-4">
        {mediaList.length > 0 ? (
          <MediaCarousel media={mediaList} />
        ) : null}
      </div>

      {/* Engagement */}
      <div className="mt-3 flex items-center justify-between text-sm text-gray-600">
        <div className="flex items-center gap-4">
          <button onClick={handleToggleLike} aria-pressed={liked} aria-label="Like post" className={`flex items-center gap-2 ${liked ? "text-red-600" : "text-gray-600"}`}>
            <span>{liked ? "❤️" : "🤍"}</span>
            <span className="font-medium">{likeCount}</span>
          </button>

          <button onClick={() => setCommentsOpen((s) => !s)} className="flex items-center gap-2 text-gray-600">
            <span>💬</span>
            <span>{post.commentCount || 0} Comments</span>
          </button>

          <div className="text-xs text-gray-500">
            {post.recentLikers && post.recentLikers.length > 0 && (
              <div className="flex items-center gap-2">
                <div className="flex -space-x-2">
                  {post.recentLikers.slice(0,3).map((l) => (
                    <img key={l.id} src={l.profileImage || "/uploads/default-avatar.png"} alt={l.name} className="w-6 h-6 rounded-full border-2 border-white" />
                  ))}
                </div>
                <div>
                  Liked by <span className="font-medium">{post.recentLikers[0].name}</span>{post.likeCount > 1 ? ` and ${post.likeCount - 1} others` : ""}
                </div>
              </div>
            )}
          </div>
        </div>

        <div className="flex items-center gap-3">
          <button className="text-gray-600">🔗 Share</button>
        </div>
      </div>

      {/* Comments preview / full */}
      {commentsOpen && (
        <div className="mt-4 border-t pt-4">
          {/* show up to 2 comments by default */}
          {(visibleComments.slice(0, post.commentCount > 2 ? 2 : visibleComments.length)).map((c) => (
            <div key={c.commentId || c.id} className="flex items-start gap-3 mb-3">
              <img src={c.author?.profileImage || "/uploads/default-avatar.png"} alt={c.author?.name} className="w-8 h-8 rounded-full object-cover" />
              <div className="flex-1">
                <div className="text-sm">
                  <span className="font-medium mr-2">{c.author?.name}</span>
                  <span className="text-xs text-gray-400">{relativeTime(c.createdAt)}</span>
                </div>
                <div className="text-sm text-gray-700 whitespace-pre-line">{c.text}</div>
                <div className="mt-1 text-xs text-gray-500">
                  <button className="mr-3">👍 {c.likeCount || 0}</button>
                  {(currentUserId === c.author?.id) && <button className="mr-3">Edit</button>}
                  {((currentUserId === c.author?.id) || userRole === "HR") && <button className="text-red-600">Delete</button>}
                </div>
              </div>
            </div>
          ))}

          {post.commentCount > 2 && (
            <button className="text-sm text-gray-600 mb-3">View all {post.commentCount} comments</button>
          )}

          {/* Add comment */}
          <div className="flex items-start gap-3">
            <img src={user?.profileImage || "/uploads/default-avatar.png"} alt={user?.email || "You"} className="w-8 h-8 rounded-full object-cover" />
            <div className="flex-1">
              <textarea value={commentText} onChange={(e) => setCommentText(e.target.value)} placeholder="Write a comment..." className="w-full border border-gray-200 rounded p-2 text-sm" rows={2} />
              <div className="flex justify-end mt-2">
                <button onClick={() => setCommentText("")} className="mr-2 text-sm text-gray-600">Cancel</button>
                <button disabled={submittingComment} onClick={handleSubmitComment} className="px-3 py-1 bg-blue-600 text-white rounded text-sm">Comment</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </article>
  );
};

export default AchievementPostCard;
