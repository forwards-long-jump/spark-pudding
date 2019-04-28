function getRequiredComponents()
  return {"position", "size"}
end

function render(g)
  for i, entity in ipairs(entities) do
    pos = entity.position
    size = entity.size
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end
end