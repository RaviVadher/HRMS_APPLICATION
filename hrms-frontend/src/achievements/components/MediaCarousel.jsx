import React, { useState } from "react";

// Minimal carousel supporting images and videos from PostMediaDTO
const MediaCarousel = ({ media = [] }) => {
  const [index, setIndex] = useState(0);

  if (!media || media.length === 0) return null;

  const current = media[index];
  const src = current.previewUrl || current.downloadUrl || current.url || current.mediaUrl || current.fileUrl || current.path || current.src;
  const type = (current.mediaType || current.mimeType || current.type || "").toLowerCase();

  const prev = () => setIndex((i) => (i - 1 + media.length) % media.length);
  const next = () => setIndex((i) => (i + 1) % media.length);

  const formatSize = (bytes) => {
    if (!bytes) return "";
    const mb = bytes / 1024 / 1024;
    if (mb < 1024) return `${mb.toFixed(2)} MB`;
    return `${(mb / 1024).toFixed(2)} GB`;
  };

  return (
    <div className="relative w-full">
      <div className="w-full bg-black/5 flex items-center justify-center max-h-[400px] overflow-hidden rounded">
        {type.includes("image") ? (
          <img src={src} alt={current.fileName || "media"} loading="lazy" className="w-full object-contain max-h-[400px]" />
        ) : (
          <video controls className="w-full max-h-[400px] bg-black">
            <source src={current.downloadUrl || src} type={current.mimeType || current.mediaType || "video/mp4"} />
            Your browser does not support the video tag.
          </video>
        )}
      </div>

      {/* Overlay info */}
      <div className="mt-2 flex items-center justify-between text-xs text-gray-500">
        <div className="flex items-center gap-3">
          <div className="font-medium">{current.fileName}</div>
          <div>{formatSize(current.fileSize)}</div>
          <div>•</div>
          <div>{new Date(current.uploadedAt).toLocaleString()}</div>
        </div>
        <div className="flex items-center gap-2">
          <a href={current.downloadUrl} download={current.fileName} target="_blank" rel="noreferrer" className="text-blue-600 hover:underline">Download</a>
        </div>
      </div>

      {/* Controls */}
      {media.length > 1 && (
        <>
          <button aria-label="Previous media" onClick={prev} className="absolute left-2 top-1/2 -translate-y-1/2 bg-white/80 rounded-full p-2 shadow">
            ◀
          </button>
          <button aria-label="Next media" onClick={next} className="absolute right-2 top-1/2 -translate-y-1/2 bg-white/80 rounded-full p-2 shadow">
            ▶
          </button>

          <div className="mt-2 flex items-center justify-center gap-2">
            {media.map((m, i) => (
              <button key={i} onClick={() => setIndex(i)} className={`w-2 h-2 rounded-full ${i === index ? "bg-blue-600" : "bg-gray-300"}`} aria-label={`Go to media ${i + 1}`} />
            ))}
            <div className="text-xs text-gray-500">{index + 1}/{media.length}</div>
          </div>
        </>
      )}
    </div>
  );
};

export default MediaCarousel;
