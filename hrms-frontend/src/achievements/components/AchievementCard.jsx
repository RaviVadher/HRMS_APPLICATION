import React, { useState } from "react";
import { toggleAchievementLike } from "../../achievementsAPI";
import "./AchievementCard.css";

const AchievementCard = ({ achievement, onLike, onShare, onDelete, isAdmin = false }) => {
  const [liked, setLiked] = useState(Boolean(achievement.isLikedByCurrentUser || achievement.isLiked || false));
  const [likeCount, setLikeCount] = useState(Number(achievement.likeCount || achievement.likes || 0));
  const [isLoading, setIsLoading] = useState(false);
  const [animateLike, setAnimateLike] = useState(false);
  const [expanded, setExpanded] = useState(false);

  const fallbackAvatar = (size = 48) => `data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='${size}' height='${size}'><rect width='100%' height='100%' fill='%23e5e7eb'/><text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' font-size='${Math.floor(size/2.2)}' fill='%236b7280'>U</text></svg>`;

  const formatDate = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toLocaleString("en-US", { month: "short", day: "numeric" });
  };

  const handleLike = async () => {
    if (isLoading) return;
    setIsLoading(true);
    setAnimateLike(true);
    setTimeout(() => setAnimateLike(false), 600);

    // optimistic
    setLiked((v) => !v);
    setLikeCount((c) => (liked ? Math.max(0, c - 1) : c + 1));

    try {
      await toggleAchievementLike(achievement.postId || achievement.id);
      if (onLike) onLike(achievement.postId || achievement.id);
    } catch (err) {
      // revert
      setLiked((v) => !v);
      setLikeCount((c) => (liked ? c + 1 : Math.max(0, c - 1)));
      console.error("Error toggling like:", err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleShare = () => {
    if (onShare) onShare(achievement.postId || achievement.id);
  };

  const handleDelete = () => {
    if (onDelete) onDelete(achievement.postId || achievement.id);
  };

  const text = achievement.description || achievement.body || "";
  const previewText = text.length > 300 ? text.slice(0, 300) + "..." : text;

  const firstMedia = (achievement.media && achievement.media.length > 0) ? achievement.media[0] : null;
  const mediaSrc = firstMedia && (firstMedia.url || firstMedia.mediaUrl || firstMedia.fileUrl || firstMedia.path || firstMedia.src);
  const mediaType = firstMedia && (firstMedia.mediaType || firstMedia.mimeType || firstMedia.type || "");

  return (
    <article className="bg-white border border-gray-200 rounded-lg shadow-sm mb-4">
      <div className="flex items-start gap-4 p-4">
            <img
              src={achievement.author?.profilePicture || achievement.employeeAvatar || fallbackAvatar(48)}
              alt={achievement.author?.firstName || achievement.employeeName || "User"}
              className="w-12 h-12 rounded-full object-cover flex-shrink-0"
              onError={(e) => {
                e.currentTarget.onerror = null;
                e.currentTarget.src = fallbackAvatar(48);
              }}
            />

        <div className="flex-1 min-w-0">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2 min-w-0">
              <div className="truncate">
                <div className="text-sm font-semibold text-gray-900 truncate">{achievement.author?.firstName ? `${achievement.author.firstName} ${achievement.author.lastName || ''}` : (achievement.employeeName || "Unknown")}</div>
                <div className="text-xs text-gray-500 truncate">{achievement.department || achievement.title || achievement.role || ""}</div>
              </div>
              <div className="text-xs text-gray-400 mx-2">•</div>
              <div className="text-xs text-gray-500">{formatDate(achievement.createdAt || achievement.date)}</div>
            </div>

            <div className="text-xs text-gray-500">{achievement.visibility || "Public"}</div>
          </div>

          {achievement.title && (<h3 className="mt-3 text-base font-semibold text-gray-900 break-words">{achievement.title}</h3>)}

          <div className="mt-2 text-sm text-gray-700">
            <p className={`leading-relaxed ${expanded ? "" : "line-clamp-4"}`}>{expanded ? text : previewText}</p>
            {text.length > 300 && (<button className="text-sm text-blue-600 mt-2" onClick={() => setExpanded((s) => !s)}>{expanded ? "Show less" : "See more"}</button>)}
          </div>
        </div>
      </div>

      {mediaSrc && (
        <div className="w-full bg-black/5">
          {mediaType && mediaType.startsWith("image") ? (
            <img src={mediaSrc} alt={achievement.title} className="w-full max-h-96 object-cover" />
          ) : (
            <video controls className="w-full max-h-96">
              <source src={mediaSrc} type={mediaType || "video/mp4"} />
              Your browser does not support the video tag.
            </video>
          )}
        </div>
      )}

      <div className="px-4 pb-4 pt-2">
        <div className="flex items-center justify-between text-sm text-gray-500 mb-2">
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-1">
              <span className="text-red-500">{liked ? "❤️" : "🤍"}</span>
              <span className="text-gray-700 font-medium">{likeCount}</span>
            </div>
            <div>{achievement.commentCount ?? (achievement.comments ? achievement.comments.length : 0)} comments</div>
          </div>

          <div className="text-xs text-gray-400">{achievement.location || ""}</div>
        </div>

        <div className="border-t border-gray-100 -mx-4 px-4 pt-2">
          <div className="flex items-center justify-between gap-2">
            <button onClick={handleLike} disabled={isLoading} className={`flex-1 inline-flex items-center justify-center gap-2 py-2 rounded-md text-sm hover:bg-gray-50 transition ${liked ? "text-blue-600" : "text-gray-600"}`}>
              <span className={`${animateLike ? "animate-heart-pop" : ""}`}>{liked ? "👍" : "👍"}</span>
              <span>Like</span>
            </button>

            <button className="flex-1 inline-flex items-center justify-center gap-2 py-2 rounded-md text-sm text-gray-600 hover:bg-gray-50 transition">💬 Comment</button>

            <button onClick={handleShare} className="flex-1 inline-flex items-center justify-center gap-2 py-2 rounded-md text-sm text-gray-600 hover:bg-gray-50 transition">🔁 Share</button>

            <button className="flex-1 inline-flex items-center justify-center gap-2 py-2 rounded-md text-sm text-gray-600 hover:bg-gray-50 transition">✉️ Send</button>
          </div>
        </div>

        {isAdmin && (
          <div className="mt-3 flex gap-2">
            <button className="text-sm text-gray-600 hover:text-blue-600">✏️ Edit</button>
            <button onClick={handleDelete} className="text-sm text-gray-600 hover:text-red-600">🗑️ Delete</button>
          </div>
        )}
      </div>
    </article>
  );
};

export default AchievementCard;
