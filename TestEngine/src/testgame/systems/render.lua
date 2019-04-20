function getRequiredComponents()
  return {"position", "size"}
end

function render(entity, g)
  pos = entity.position
  size = entity.size
  g:fillRect(pos.x, pos.y, size.width, size.height)
end